package com.es.cinema.tickets.persistence.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusAssento {

    DISPONIVEL("disponivel"),
    OCUPADO("ocupado"),
    VENDIDO("vendido");

    private final String valor;

    StatusAssento(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }
}
