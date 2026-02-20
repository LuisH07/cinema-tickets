package com.es.cinema.tickets.exception.business;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyRegisteredException extends ApiException {
    public EmailAlreadyRegisteredException(String email) {
        super("EMAIL_ALREADY_REGISTERED", HttpStatus.CONFLICT, "Conflito de dados",
                "Email já cadastrado: " + email);
    }
}
