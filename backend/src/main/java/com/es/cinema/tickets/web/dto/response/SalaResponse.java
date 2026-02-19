package com.es.cinema.tickets.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalaResponseDTO {
    private Long id;
    private String nome;
    private int capacidade;
}
