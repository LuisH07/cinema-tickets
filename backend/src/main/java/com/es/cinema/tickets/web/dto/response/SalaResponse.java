package com.es.cinema.tickets.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalaResponse {
    private long id;
    private String nome;
    private int capacidade;
}
