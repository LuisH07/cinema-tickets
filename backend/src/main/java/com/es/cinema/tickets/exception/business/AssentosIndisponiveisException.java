package com.es.cinema.tickets.exception.business;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class AssentosIndisponiveisException extends ApiException {

    public AssentosIndisponiveisException(List<String> codigos) {
        super(
                "ASSENTOS_INDISPONIVEIS",
                HttpStatus.CONFLICT,
                "Conflito de reserva",
                "Os seguintes assentos não estão mais disponíveis: " + String.join(", ", codigos)
        );
    }
}
