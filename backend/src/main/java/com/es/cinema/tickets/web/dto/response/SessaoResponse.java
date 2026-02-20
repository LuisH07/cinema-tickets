package com.es.cinema.tickets.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SessaoResponse {
    private long id;

    private long filmeId;
    private long salaId;

    private LocalDateTime inicio;
    private String tipo;
}
