package com.es.cinema.tickets.web.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SessaoRequest {

    @NotNull(message = "Filme é obrigatório")
    private Long filmeId;

    @NotNull(message = "Sala é obrigatória")
    private Long salaId;

    @Future
    @NotNull(message = "Início é obrigatório")
    private LocalDateTime inicio; // ex: "2026-02-19T19:30:00"

    @NotBlank(message = "Tipo é obrigatório")
    private String tipo;
}
