package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.StatusAssento;
import com.es.cinema.tickets.persistence.enums.TipoAssento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AssentoSessaoTest {

    @Test
    void testGettersSettersAndMethods() {
        Sessao sessao = new Sessao(); // mock ou instancia simples
        AssentoSessao assento = AssentoSessao.builder()
                .id(1L)
                .sessao(sessao)
                .codigo("A1")
                .tipo(TipoAssento.COMUM)
                .valor(BigDecimal.valueOf(20))
                .status(StatusAssento.DISPONIVEL)
                .ingressoId(null)
                .build();

        // Getters
        assertEquals(1L, assento.getId());
        assertEquals(sessao, assento.getSessao());
        assertEquals("A1", assento.getCodigo());
        assertEquals(TipoAssento.COMUM, assento.getTipo());
        assertEquals(BigDecimal.valueOf(20), assento.getValor());
        assertEquals(StatusAssento.DISPONIVEL, assento.getStatus());
        assertNull(assento.getIngressoId());

        // Métodos de conveniência
        assertTrue(assento.isDisponivel());

        assento.ocupar();
        assertEquals(StatusAssento.OCUPADO, assento.getStatus());

        assento.vender("ING123");
        assertEquals(StatusAssento.VENDIDO, assento.getStatus());
        assertEquals("ING123", assento.getIngressoId());

        assento.liberar();
        assertEquals(StatusAssento.DISPONIVEL, assento.getStatus());
        assertNull(assento.getIngressoId());

        // toString
        String str = assento.toString();
        assertTrue(str.contains("AssentoSessao"));
        assertTrue(str.contains("A1"));

        // equals e hashCode
        AssentoSessao assento2 = AssentoSessao.builder().id(1L).build();
        assertEquals(assento, assento2);
        assertEquals(assento.hashCode(), assento2.hashCode());

        AssentoSessao assento3 = AssentoSessao.builder().id(2L).build();
        assertNotEquals(assento, assento3);
    }
}