package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.business.AssentosIndisponiveisException;
import com.es.cinema.tickets.exception.business.ValorDivergenteException;
import com.es.cinema.tickets.exception.notfound.SessaoNotFoundException;
import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.persistence.entity.Pedido;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.repository.AssentoSessaoRepository;
import com.es.cinema.tickets.persistence.repository.PedidoRepository;
import com.es.cinema.tickets.persistence.repository.SessaoRepository;
import com.es.cinema.tickets.web.dto.request.PagamentoRequest;
import com.es.cinema.tickets.web.dto.response.PagamentoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final SessaoRepository sessaoRepository;
    private final AssentoSessaoRepository assentoSessaoRepository;
    private final PedidoRepository pedidoRepository;

    // Processa o pagamento dos ingressos dentro de uma única transação SQL.
    @Transactional
    public PagamentoResponse processar(PagamentoRequest request) {
        Sessao sessao = sessaoRepository.findById(request.getSessaoId())
                .orElseThrow(() -> new SessaoNotFoundException(request.getSessaoId()));

        // Adquire bloqueio pessimista nos assentos para evitar dupla compra concorrente
        List<AssentoSessao> assentos = assentoSessaoRepository
                .findAllByIdWithLock(request.getAssentosIds());

        validarQuantidade(request.getAssentosIds(), assentos);
        validarDisponibilidade(assentos);
        validarValorTotal(request.getValorEsperado(), assentos);

        // Marca todos os assentos como VENDIDO dentro da mesma transação
        List<String> ingressosIds = assentos.stream()
                .map(assento -> {
                    String ingressoId = gerarIngressoId();
                    assento.vender(ingressoId);
                    return ingressoId;
                })
                .toList();

        assentoSessaoRepository.saveAll(assentos);

        // Calcula o valor total real
        BigDecimal valorTotal = assentos.stream()
                .map(AssentoSessao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Persiste o pedido como PAGO na mesma transação
        Pedido pedido = Pedido.builder()
                .sessao(sessao)
                .valorTotal(valorTotal)
                .metodo(request.getMetodo())
                .assentos(new HashSet<>(assentos))
                .build();
        pedido.aprovar(String.join(",", ingressosIds));

        pedidoRepository.save(pedido);

        return PagamentoResponse.builder()
                .status("aprovado")
                .mensagem("Compra realizada com sucesso!")
                .ingressosIds(ingressosIds)
                .build();
    }

    // Garante que todos os IDs de assentos solicitados foram encontrados no banco.
    private void validarQuantidade(List<Long> idsRequisitados, List<AssentoSessao> encontrados) {
        if (encontrados.size() != idsRequisitados.size()) {
            throw new AssentosIndisponiveisException(List.of("Um ou mais IDs de assentos são inválidos"));
        }
    }

    // Verifica se algum assento foi ocupado ou vendido entre a listagem e o pagamento.
    private void validarDisponibilidade(List<AssentoSessao> assentos) {
        List<String> indisponiveis = assentos.stream()
                .filter(a -> !a.isDisponivel())
                .map(AssentoSessao::getCodigo)
                .toList();

        if (!indisponiveis.isEmpty()) {
            throw new AssentosIndisponiveisException(indisponiveis);
        }
    }

    private void validarValorTotal(BigDecimal valorEsperado, List<AssentoSessao> assentos) {
        BigDecimal valorCalculado = assentos.stream()
                .map(AssentoSessao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (valorCalculado.compareTo(valorEsperado) != 0) {
            throw new ValorDivergenteException(valorEsperado, valorCalculado);
        }
    }

    private String gerarIngressoId() {
        String ano = String.valueOf(Year.now().getValue());
        String sufixo = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "TKT-" + ano + "-" + sufixo;
    }
}
