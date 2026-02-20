package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.notfound.FilmeNotFoundException;
import com.es.cinema.tickets.exception.notfound.SalaNotFoundException;
import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.repository.SalaRepository;
import com.es.cinema.tickets.web.dto.response.SalaResponse;
import com.es.cinema.tickets.web.dto.response.SessaoResponse;
import com.es.cinema.tickets.web.mapper.SalaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final SalaMapper salaMapper;

    @Transactional(readOnly = true)
    public List<SalaResponse> listarTodas() {
        return salaRepository.findAll()
                .stream()
                .map(salaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Sala getOrThrow(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new SalaNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public SalaResponse buscarPorId(Long id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new SalaNotFoundException(id));

        return salaMapper.toResponse(sala);
    }
}
