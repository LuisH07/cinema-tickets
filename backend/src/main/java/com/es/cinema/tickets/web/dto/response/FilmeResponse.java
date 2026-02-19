package com.es.cinema.tickets.web.dto.response;

import com.es.cinema.tickets.persistence.enums.StatusFilme;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilmeResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Integer duracao;
    private StatusFilme status;
}
