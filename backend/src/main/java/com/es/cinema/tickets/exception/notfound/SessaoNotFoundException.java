package com.es.cinema.tickets.exception.notfound;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SessaoNotFoundException extends ApiException {
    public SessaoNotFoundException(Long id) {
        super(
                "SESSAO_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                "Sessão não encontrada com id: " + id
        );
    }
}
