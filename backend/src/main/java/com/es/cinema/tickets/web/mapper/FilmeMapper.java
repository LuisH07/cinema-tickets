package com.es.cinema.tickets.web.mapper;

import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.web.dto.response.FilmeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilmeMapper {

    // TODO implementar CRUD de filmes futuramente
//    public Filme toEntity(FilmeRequest request) {
//        return Filme.builder()
//                .titulo(request.getTitulo())
//                .sinopse(request.getSinopse())
//                .duracao(request.getDuracao())
//                .status(request.getStatus())
//                .build()
//    }

    public FilmeResponse toResponse(Filme filme) {
        return FilmeResponse.builder()
                .id(filme.getId())
                .titulo(filme.getTitulo())
                .poster(filme.getPoster())
                .backdrop(filme.getBackdrop())
                .classificacao(filme.getClassificacao())
                .duracao(filme.getDuracao())
                .generos(filme.getGeneros())
                .diretores(filme.getDiretores())
                .sinopse(filme.getSinopse())
                .elenco(filme.getElenco())
                .status(filme.getStatus())
                .build();
    }

    public List<FilmeResponse> toResponseList(List<Filme> filmes) {
        return filmes.stream()
                .map(this::toResponse)
                .toList();
    }
}
