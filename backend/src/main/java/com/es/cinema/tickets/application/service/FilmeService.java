package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.notfound.FilmeNotFoundException;
import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.repository.FilmeRepository;
import com.es.cinema.tickets.web.dto.response.FilmeResponse;
import com.es.cinema.tickets.web.mapper.FilmeMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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
        List<Filme> filmes = filmeRepository.findAll();
        filmes.forEach(this::initializeCollections);
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
        initializeCollections(filme);
        return filmeMapper.toResponse(filme);
    }

    private void initializeCollections(Filme filme) {
        Hibernate.initialize(filme.getGeneros());
        Hibernate.initialize(filme.getDiretores());
        Hibernate.initialize(filme.getElenco());
    }
}
