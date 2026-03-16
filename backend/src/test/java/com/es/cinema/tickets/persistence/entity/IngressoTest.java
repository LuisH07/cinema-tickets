package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.StatusIngresso;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IngressoTest {

    @Test
    void testGettersSettersAndMethods() {
        Pedido pedido = new Pedido(); // instância simples para teste
        User usuario = User.builder()
                .id(1L)
                .email("teste@example.com")
                .passwordHash("senha")
                .role(com.es.cinema.tickets.persistence.enums.Role.USER)
                .nome("Luis")
                .cpf("12345678900")
                .build();

        Ingresso ingresso = Ingresso.builder()
                .id(1L)
                .codigo("ABC123")
                .codigoAutenticacao("AUT123")
                .status(StatusIngresso.CONFIRMADO)
                .pedido(pedido)
                .usuario(usuario)
                .criadoEm(LocalDateTime.now())
                .build();

        // Getters e setters
        assertEquals(1L, ingresso.getId());
        assertEquals("ABC123", ingresso.getCodigo());
        assertEquals("AUT123", ingresso.getCodigoAutenticacao());
        assertEquals(StatusIngresso.CONFIRMADO, ingresso.getStatus());
        assertEquals(pedido, ingresso.getPedido());
        assertEquals(usuario, ingresso.getUsuario());
        assertNotNull(ingresso.getCriadoEm());

        // Testando métodos de conveniência
        ingresso.utilizar();
        assertEquals(StatusIngresso.UTILIZADO, ingresso.getStatus());
        assertNotNull(ingresso.getDataHoraEntrada());

        ingresso.avaliar();
        assertEquals(StatusIngresso.AVALIADO, ingresso.getStatus());

        // toString
        String str = ingresso.toString();
        assertTrue(str.contains("Ingresso"));
        assertTrue(str.contains("ABC123"));

        // equals e hashCode
        Ingresso ingresso2 = Ingresso.builder().id(1L).build();
        assertEquals(ingresso, ingresso2);
        assertEquals(ingresso.hashCode(), ingresso2.hashCode());

        Ingresso ingresso3 = Ingresso.builder().id(2L).build();
        assertNotEquals(ingresso, ingresso3);
    }
}