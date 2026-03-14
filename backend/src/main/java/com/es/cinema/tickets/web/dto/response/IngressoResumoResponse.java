package com.es.cinema.tickets.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class IngressoResumoResponse {
    private String id;
    private String filme;
    private String data;
    private String horario;
    private String sala;
    private List<String> assentos;
    private String status;
}
