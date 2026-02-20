package com.es.cinema.tickets.web.dto.response;

import com.es.cinema.tickets.persistence.enums.StatusFilme;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FilmeResponse {
    private long id;
    private String titulo;
    private String poster;
    private String backdrop;
    private String classificacao;
    private int duracao;
    private List<String> generos;
    private List<String> diretores;
    private String sinopse;
    private List<String> elenco;
    private StatusFilme status;
}
