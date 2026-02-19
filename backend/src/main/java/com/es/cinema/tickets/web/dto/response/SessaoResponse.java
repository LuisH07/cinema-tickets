package com.es.cinema.tickets.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SessaoResponseDTO {
    private Long id;
    private LocalDateTime inicio;
    private String classificacao;
    private Long filmeId;
    private String filmeTitulo;
    private Long salaId;
    private int salaNumero;
    private int totalAssentos;
}
