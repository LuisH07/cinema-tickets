package com.es.cinema.tickets.exception.notfound;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class IngressoNotFoundException extends ApiException {
    public IngressoNotFoundException(String codigo) {
        super(
                "INGRESSO_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                "Ingresso não encontrado com código: " + codigo
        );
    }
}
