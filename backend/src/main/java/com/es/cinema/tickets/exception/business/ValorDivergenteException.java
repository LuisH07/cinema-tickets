package com.es.cinema.tickets.exception.business;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class ValorDivergenteException extends ApiException {

    public ValorDivergenteException(BigDecimal esperado, BigDecimal calculado) {
        super(
                "VALOR_DIVERGENTE",
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Valor inconsistente",
                "O valor esperado R$ " + esperado + " diverge do valor calculado R$ " + calculado
        );
    }
}
