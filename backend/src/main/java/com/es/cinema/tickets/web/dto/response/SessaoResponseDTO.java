package com.es.cinema.tickets.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class SessaoResponseDTO {
    private Long id;
    private LocalDate data;
    private LocalTime horario;
    private String classificacao;
    private Long filmeId;
    private String filmeTitulo;
    private Long salaId;
    private Integer salaNumero;
    private Integer totalAssentos;
}
