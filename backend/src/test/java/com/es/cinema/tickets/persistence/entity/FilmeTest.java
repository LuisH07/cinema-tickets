package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.StatusFilme;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmeTest {

    @Test
    void testGettersSettersAndMethods() {
        Filme filme = Filme.builder()
                .id(1L)
                .titulo("Filme Teste")
                .poster("poster.png")
                .backdrop("backdrop.png")
                .classificacao("12+")
                .duracao(120)
                .generos(List.of("Ação", "Comédia"))
                .diretores(List.of("Diretor 1"))
                .elenco(List.of("Ator 1", "Ator 2"))
                .sinopse("Uma sinopse de teste")
                .status(StatusFilme.EM_CARTAZ)
                .mediaAvaliacao(4.5)
                .qtdAvaliacoes(2)
                .build();

        // Getters
        assertEquals(1L, filme.getId());
        assertEquals("Filme Teste", filme.getTitulo());
        assertEquals("poster.png", filme.getPoster());
        assertEquals("backdrop.png", filme.getBackdrop());
        assertEquals("12+", filme.getClassificacao());
        assertEquals(120, filme.getDuracao());
        assertEquals(List.of("Ação", "Comédia"), filme.getGeneros());
        assertEquals(List.of("Diretor 1"), filme.getDiretores());
        assertEquals(List.of("Ator 1", "Ator 2"), filme.getElenco());
        assertEquals("Uma sinopse de teste", filme.getSinopse());
        assertEquals(StatusFilme.EM_CARTAZ, filme.getStatus());
        assertEquals(4.5, filme.getMediaAvaliacao());
        assertEquals(2, filme.getQtdAvaliacoes());

        // Testando registrarAvaliacao
        filme.registrarAvaliacao(5);
        assertEquals(3, filme.getQtdAvaliacoes());
        assertEquals((4.5*2 + 5)/3, filme.getMediaAvaliacao());

        // toString
        String str = filme.toString();
        assertTrue(str.contains("Filme"));
        assertTrue(str.contains("Filme Teste"));

        // equals e hashCode
        Filme filme2 = Filme.builder().id(1L).build();
        assertEquals(filme, filme2);
        assertEquals(filme.hashCode(), filme2.hashCode());

        Filme filme3 = Filme.builder().id(2L).build();
        assertNotEquals(filme, filme3);
    }
}