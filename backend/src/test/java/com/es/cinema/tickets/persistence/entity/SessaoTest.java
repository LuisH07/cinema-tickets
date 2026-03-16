package com.es.cinema.tickets.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SessaoTest {

    @Test
    void testSessaoEntity() {
        Filme filme = Filme.builder()
                .id(1L)
                .titulo("Filme Teste")
                .poster("poster.jpg")
                .backdrop("backdrop.jpg")
                .classificacao("12")
                .duracao(120)
                .sinopse("Sinopse")
                .status(com.es.cinema.tickets.persistence.enums.StatusFilme.EM_BREVE)
                .build();

        Sala sala = Sala.builder()
                .id(1L)
                .nome("Sala 1")
                .capacidade(100)
                .build();

        LocalDateTime inicio = LocalDateTime.of(2026, 3, 16, 19, 30);

        Sessao sessao = Sessao.builder()
                .id(1L)
                .filme(filme)
                .sala(sala)
                .inicio(inicio)
                .tipo("2D")
                .build();

        // Testando getters
        assertEquals(1L, sessao.getId());
        assertEquals(filme, sessao.getFilme());
        assertEquals(sala, sessao.getSala());
        assertEquals(inicio, sessao.getInicio());
        assertEquals("2D", sessao.getTipo());

        // toString
        String str = sessao.toString();
        assertTrue(str.contains("Sessao"));
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("inicio=" + inicio));

        // equals e hashCode
        Sessao mesmaId = Sessao.builder().id(1L).build();
        assertEquals(sessao, mesmaId);
        assertEquals(sessao.hashCode(), mesmaId.hashCode());

        Sessao diferenteId = Sessao.builder().id(2L).build();
        assertNotEquals(sessao, diferenteId);
        assertNotEquals(sessao.hashCode(), diferenteId.hashCode());
    }
}