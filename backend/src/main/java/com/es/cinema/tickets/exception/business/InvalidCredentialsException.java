package com.es.cinema.tickets.exception.business;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super(
                "INVALID_CREDENTIALS",
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas",
                "Email ou senha incorretos."
        );
    }
}
