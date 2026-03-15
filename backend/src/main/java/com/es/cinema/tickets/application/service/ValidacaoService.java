package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.persistence.entity.Ingresso;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.enums.StatusIngresso;
import com.es.cinema.tickets.persistence.enums.StatusPedido;
import com.es.cinema.tickets.persistence.repository.IngressoRepository;
import com.es.cinema.tickets.web.dto.response.ValidacaoIngressoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidacaoService {

    private final IngressoRepository ingressoRepository;

    @Value("${app.ingresso.tolerancia-horas:3}")
    private int toleranciaHoras;

    @Transactional
    public ValidacaoIngressoResponse validar(String codigoVoucher) {
        Optional<Ingresso> optional = ingressoRepository.findByCodigoComDetalhes(codigoVoucher);

        if (optional.isEmpty()) {
            log.warn("Tentativa de validação com código inválido");
            return ValidacaoIngressoResponse.falha("Código inválido");
        }

        Ingresso ingresso = optional.get();

        // Verifica se está PAGO
        if (!StatusPedido.PAGO.equals(ingresso.getPedido().getStatus())) {
            log.warn("Validação negada: pedido não está pago. ingressoId={}", ingresso.getId());
            return ValidacaoIngressoResponse.falha("Pagamento não confirmado para este ingresso");
        }

        // Impede validação de ingresso cancelado
        if (StatusIngresso.CANCELADO.equals(ingresso.getStatus())) {
            log.warn("Validação negada: ingresso cancelado. ingressoId={}", ingresso.getId());
            return ValidacaoIngressoResponse.falha("Ingresso cancelado");
        }

        // Impede dupla entrada
        if (StatusIngresso.UTILIZADO.equals(ingresso.getStatus())) {
            log.warn("Validação negada: ingresso já utilizado. ingressoId={}", ingresso.getId());
            return ValidacaoIngressoResponse.falha("Ingresso já utilizado");
        }

        // Valida se a sessão está dentro da margem de tolerância
        Sessao sessao = ingresso.getPedido().getSessao();
        if (!dentroDoHorario(sessao.getInicio())) {
            log.warn("Validação negada: sessão fora do período de tolerância. sessaoId={}, inicio={}",
                    sessao.getId(), sessao.getInicio());
            return ValidacaoIngressoResponse.falha(
                    "Sessão fora do período de validação. Tolerância: " + toleranciaHoras + "h antes/após o início");
        }

        // Registra a entrada
        ingresso.utilizar();
        ingressoRepository.save(ingresso);

        log.info("Ingresso validado com sucesso. ingressoId={}, usuarioId={}",
                ingresso.getId(), ingresso.getUsuario().getId());

        List<String> assentos = ingresso.getPedido().getAssentos().stream()
                .map(AssentoSessao::getCodigo)
                .sorted()
                .toList();

        return ValidacaoIngressoResponse.sucesso(
                ingresso.getStatus().name(),
                ingresso.getUsuario().getNome(),
                sessao.getFilme().getTitulo(),
                sessao.getSala().getNome(),
                assentos,
                ingresso.getDataHoraEntrada()
        );
    }

    // Permite validar dentro do intervalo (inicio - tolerancia, inicio + tolerancia)
    private boolean dentroDoHorario(LocalDateTime inicioSessao) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limiteInferior = inicioSessao.minusHours(toleranciaHoras);
        LocalDateTime limiteSuperior = inicioSessao.plusHours(toleranciaHoras);
        return !agora.isBefore(limiteInferior) && !agora.isAfter(limiteSuperior);
    }
}
