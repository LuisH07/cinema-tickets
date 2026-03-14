package com.es.cinema.tickets.exception.business;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class IngressoAcessoNegadoException extends ApiException {
    public IngressoAcessoNegadoException() {
        super(
                "INGRESSO_ACESSO_NEGADO",
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                "Você não tem permissão para acessar este ingresso."
        );
    }
}
