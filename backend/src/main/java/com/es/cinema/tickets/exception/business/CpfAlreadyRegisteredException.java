package com.es.cinema.tickets.exception.business;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class CpfAlreadyRegisteredException extends ApiException {
    public CpfAlreadyRegisteredException(String cpf) {
        super(
                "CPF_ALREADY_REGISTERED",
                HttpStatus.CONFLICT,
                "Conflito de dados",
                "CPF já cadastrado: " + cpf
        );
    }
}
