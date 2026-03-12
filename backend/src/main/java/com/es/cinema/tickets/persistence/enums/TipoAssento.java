package com.es.cinema.tickets.persistence.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoAssento {

    COMUM("comum"),
    PNE("pne"),
    VIP("vip");

    private final String valor;

    TipoAssento(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }
}
