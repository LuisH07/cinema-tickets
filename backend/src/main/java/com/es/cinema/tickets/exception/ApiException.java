package com.es.cinema.tickets.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String title;

    protected ApiException(String errorCode, HttpStatus httpStatus, String title, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.title = title;
    }
}
