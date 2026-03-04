package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.notfound.SessaoNotFoundException;
import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.enums.StatusAssento;
import com.es.cinema.tickets.persistence.enums.TipoAssento;
import com.es.cinema.tickets.persistence.repository.AssentoSessaoRepository;
import com.es.cinema.tickets.persistence.repository.SessaoRepository;
import com.es.cinema.tickets.web.dto.request.SessaoRequest;
import com.es.cinema.tickets.web.dto.response.SessaoResponse;
import com.es.cinema.tickets.web.mapper.SessaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessaoService {

    private static final int ASSENTOS_POR_FILEIRA = 10;

    // Fileiras VIP
    private static final int FILEIRAS_VIP = 2;

    // Últimos N assentos de cada fileira reservados para PNE
    private static final int ASSENTOS_PNE_POR_FILEIRA = 2;

    private static final BigDecimal VALOR_COMUM = new BigDecimal("25.00");
    private static final BigDecimal VALOR_VIP    = new BigDecimal("40.00");
    private static final BigDecimal VALOR_PNE    = new BigDecimal("25.00");

    private final SessaoRepository sessaoRepository;
    private final SessaoMapper sessaoMapper;

    private final AssentoSessaoRepository assentoSessaoRepository;

    private final FilmeService filmeService;

    private final SalaService salaService;

    @Transactional
    public SessaoResponse criar(SessaoRequest sessaoRequest) {
        Filme filme = filmeService.getOrThrow(sessaoRequest.getFilmeId());
        Sala sala = salaService.getOrThrow(sessaoRequest.getSalaId());

        Sessao sessao = sessaoMapper.toEntity(sessaoRequest, filme, sala);

        // Salva primeiro a sessão
        Sessao sessaoSalva = sessaoRepository.save(sessao);

        // Gera assentos com referência para a sessão salva
        List<AssentoSessao> assentos = gerarAssentos(sessaoSalva, sala.getCapacidade());

        // Salva os assentos explicitamente
        assentoSessaoRepository.saveAll(assentos);

        return sessaoMapper.toResponse(sessaoSalva);
    }

    private List<AssentoSessao> gerarAssentos(Sessao sessao, int capacidade) {
        List<AssentoSessao> assentos = new ArrayList<>();
        int fileiras = (int) Math.ceil((double) capacidade / ASSENTOS_POR_FILEIRA);

        int assentosCriados = 0;
        for (int f = 0; f < fileiras && assentosCriados < capacidade; f++) {
            char letra = (char) ('A' + f);
            int assentosDaFileira = Math.min(ASSENTOS_POR_FILEIRA, capacidade - assentosCriados);

            boolean fileiravip = f < FILEIRAS_VIP;

            for (int n = 1; n <= assentosDaFileira; n++) {
                boolean isPne = !fileiravip && (n > assentosDaFileira - ASSENTOS_PNE_POR_FILEIRA);

                TipoAssento tipo;
                BigDecimal valor;

                if (fileiravip) {
                    tipo = TipoAssento.VIP;
                    valor = VALOR_VIP;
                } else if (isPne) {
                    tipo = TipoAssento.PNE;
                    valor = VALOR_PNE;
                } else {
                    tipo = TipoAssento.COMUM;
                    valor = VALOR_COMUM;
                }

                AssentoSessao assento = AssentoSessao.builder()
                        .sessao(sessao)
                        .codigo(letra + String.valueOf(n))
                        .tipo(tipo)
                        .valor(valor)
                        .status(StatusAssento.DISPONIVEL)
                        .build();
                assentos.add(assento);
                assentosCriados++;
            }
        }
        return assentos;
    }

    @Transactional(readOnly = true)
    public List<SessaoResponse> listarPorData(LocalDate data) {

        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime inicioProximoDia = data.plusDays(1).atStartOfDay();

        return sessaoMapper.toResponseList(
                sessaoRepository.findByInicioBetween(inicioDoDia, inicioProximoDia)
        );
    }

    @Transactional(readOnly = true)
    public SessaoResponse buscarPorId(Long id) {
        Sessao sessao = sessaoRepository.findWithFilmeAndSalaById(id)
                .orElseThrow(() -> new SessaoNotFoundException(id));

        return sessaoMapper.toResponse(sessao);
    }
}
