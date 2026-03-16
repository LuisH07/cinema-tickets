package com.es.cinema.tickets.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalaTest {

    @Test
    void testSalaEntity() {
        // Criando sala via builder
        Sala sala = Sala.builder()
                .id(1L)
                .nome("Sala VIP")
                .capacidade(100)
                .build();

        // Testando getters
        assertEquals(1L, sala.getId());
        assertEquals("Sala VIP", sala.getNome());
        assertEquals(100, sala.getCapacidade());

        // toString
        String str = sala.toString();
        assertTrue(str.contains("Sala"));
        assertTrue(str.contains("Sala VIP"));
        assertTrue(str.contains("100"));

        // equals e hashCode
        Sala mesmoId = Sala.builder().id(1L).build();
        assertEquals(sala, mesmoId);
        assertEquals(sala.hashCode(), mesmoId.hashCode());

        Sala diferenteId = Sala.builder().id(2L).build();
        assertNotEquals(sala, diferenteId);
        assertNotEquals(sala.hashCode(), diferenteId.hashCode());
    }
}