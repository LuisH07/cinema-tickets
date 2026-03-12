package com.es.cinema.tickets.web.dto.response;

import com.es.cinema.tickets.persistence.enums.StatusAssento;
import com.es.cinema.tickets.persistence.enums.TipoAssento;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AssentoResponse {
    private Long id;
    private String codigo;
    private StatusAssento status;
    private TipoAssento tipo;
    private BigDecimal valor;
}
