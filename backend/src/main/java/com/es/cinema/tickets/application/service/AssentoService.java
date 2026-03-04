package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.notfound.SessaoNotFoundException;
import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.persistence.repository.AssentoSessaoRepository;
import com.es.cinema.tickets.persistence.repository.SessaoRepository;
import com.es.cinema.tickets.web.dto.response.SessaoAssentosResponse;
import com.es.cinema.tickets.web.mapper.AssentoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssentoService {

    private final AssentoSessaoRepository assentoSessaoRepository;
    private final SessaoRepository sessaoRepository;
    private final AssentoMapper assentoMapper;

    @Transactional(readOnly = true)
    public SessaoAssentosResponse listarPorSessao(Long sessaoId) {
        if (!sessaoRepository.existsById(sessaoId)) {
            throw new SessaoNotFoundException(sessaoId);
        }

        List<AssentoSessao> assentos = assentoSessaoRepository.findBySessaoIdOrderByCodigoAsc(sessaoId);

        return assentoMapper.toSessaoAssentosResponse(sessaoId, assentos);
    }
}
