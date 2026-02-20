package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.notfound.FilmeNotFoundException;
import com.es.cinema.tickets.exception.notfound.SalaNotFoundException;
import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.repository.FilmeRepository;
import com.es.cinema.tickets.web.dto.response.FilmeResponse;
import com.es.cinema.tickets.web.dto.response.SalaResponse;
import com.es.cinema.tickets.web.mapper.FilmeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmeService {

    private final FilmeRepository filmeRepository;
    private final FilmeMapper filmeMapper;

    @Transactional(readOnly = true)
    public List<FilmeResponse> listarTodos() {
        List<Filme> filmes = filmeRepository.findAllWithGeneros();
        filmes = filmeRepository.findAllWithDiretores();
        filmes = filmeRepository.findAllWithElenco();
        
        return filmes.stream()
            .map(filmeMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public Filme getOrThrow(Long id) {
        return filmeRepository.findById(id)
                .orElseThrow(() -> new FilmeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public FilmeResponse buscarPorId(Long id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new FilmeNotFoundException(id));

        return filmeMapper.toResponse(filme);
    }
}
