package com.es.cinema.tickets.persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MetodoPagamento {

    CARTAO_CREDITO("cartao_credito"),
    CARTAO_DEBITO("cartao_debito"),
    PIX("pix"),
    DINHEIRO("dinheiro");

    private final String valor;

    MetodoPagamento(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static MetodoPagamento from(String value) {
        if (value == null) {
            return null;
        }
        for (MetodoPagamento metodo : values()) {
            if (metodo.valor.equalsIgnoreCase(value) || metodo.name().equalsIgnoreCase(value)) {
                return metodo;
            }
        }
        throw new IllegalArgumentException("Método de pagamento inválido: " + value);
    }
}
