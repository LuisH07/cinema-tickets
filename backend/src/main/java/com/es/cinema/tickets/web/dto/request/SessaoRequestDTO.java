package com.es.cinema.tickets.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class SessaoRequestDTO {

    @NotNull(message = "Filme é obrigatório")
    private Long filmeId;

    @NotNull(message = "Sala é obrigatória")
    private Long salaId;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @NotNull(message = "Horário é obrigatório")
    private LocalTime horario;

    @NotBlank(message = "Classificação é obrigatória")
    private String classificacao;
}
