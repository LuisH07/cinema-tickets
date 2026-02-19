package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.repository.SessaoRepository;
import com.es.cinema.tickets.web.dto.request.SessaoRequestDTO;
import com.es.cinema.tickets.web.dto.response.SessaoResponseDTO;
import com.es.cinema.tickets.web.mapper.SessaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessaoService {

    private static final int ASSENTOS_POR_FILEIRA = 10;

    private final SessaoRepository sessaoRepository;
    private final FilmeService filmeService;
    private final SalaService salaService;

    @Transactional
    public SessaoResponseDTO criar(SessaoRequestDTO dto) {
        Filme filme = filmeService.buscarPorId(dto.getFilmeId());
        Sala sala = salaService.buscarPorId(dto.getSalaId());

        Sessao sessao = SessaoMapper.toEntity(dto, filme, sala);
        List<AssentoSessao> assentos = gerarAssentos(sessao, sala.getCapacidade());
        sessao.getAssentos().addAll(assentos);

        Sessao sessaoSalva = sessaoRepository.save(sessao);
        return SessaoMapper.toResponseDTO(sessaoSalva);
    }

    private List<AssentoSessao> gerarAssentos(Sessao sessao, int capacidade) {
        List<AssentoSessao> assentos = new ArrayList<>();
        int fileiras = (int) Math.ceil((double) capacidade / ASSENTOS_POR_FILEIRA);

        int assentosCriados = 0;
        for (int f = 0; f < fileiras && assentosCriados < capacidade; f++) {
            char letra = (char) ('A' + f);
            int assentosDaFileira = Math.min(ASSENTOS_POR_FILEIRA, capacidade - assentosCriados);
            for (int n = 1; n <= assentosDaFileira; n++) {
                AssentoSessao assento = AssentoSessao.builder()
                        .sessao(sessao)
                        .codigo(letra + String.valueOf(n))
                        .ocupado(false)
                        .build();
                assentos.add(assento);
                assentosCriados++;
            }
        }
        return assentos;
    }
}
