package com.es.cinema.tickets.exception.business;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class IngressoNaoUtilizadoException extends ApiException {
    public IngressoNaoUtilizadoException() {
        super(
                "INGRESSO_NAO_UTILIZADO",
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Avaliação não permitida",
                "O ingresso só pode ser avaliado após a validação de entrada na sessão."
        );
    }
}
