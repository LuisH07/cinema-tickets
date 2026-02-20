package com.es.cinema.tickets.exception.notfound;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FilmeNotFoundException extends ApiException {
    public FilmeNotFoundException(Long id) {
        super(
                "FILME_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                "Filme não encontrado com id: " + id
        );
    }
}
