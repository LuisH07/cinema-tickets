package com.es.cinema.tickets.persistence.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusPedido {

    PENDENTE("pendente"),
    PAGO("pago"),
    CANCELADO("cancelado");

    private final String valor;

    StatusPedido(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }
}
