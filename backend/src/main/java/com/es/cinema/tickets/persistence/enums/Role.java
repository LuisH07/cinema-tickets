package com.es.cinema.tickets.persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN,
    USER;

    @JsonCreator
    public static Role from(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role: " + value);
        }
    }

}