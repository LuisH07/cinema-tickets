package com.es.cinema.tickets.exception.notfound;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SalaNotFoundException extends ApiException {
    public SalaNotFoundException(Long id) {
        super(
                "SALA_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                "Sala não encontrada com id: " + id
        );
    }
}
