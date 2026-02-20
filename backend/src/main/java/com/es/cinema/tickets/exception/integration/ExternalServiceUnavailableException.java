package com.es.cinema.tickets.exception.integration;

import com.es.cinema.tickets.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ExternalServiceUnavailableException extends ApiException {
    public ExternalServiceUnavailableException(String serviceName, String detailMessage) {
        super("EXTERNAL_SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE,
                "Serviço externo indisponível", serviceName + ": " + detailMessage);
    }
}
