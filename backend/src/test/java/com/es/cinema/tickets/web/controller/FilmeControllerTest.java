package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.FilmeService;
import com.es.cinema.tickets.web.dto.response.FilmeResponse;
import com.es.cinema.tickets.persistence.enums.StatusFilme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmeControllerTest {

    private FilmeService filmeService;
    private FilmeController filmeController;

    @BeforeEach
    void setUp() {
        filmeService = mock(FilmeService.class);
        filmeController = new FilmeController(filmeService);
    }

    @Test
    void listarTodos_shouldReturnListFromService() {
        FilmeResponse f1 = FilmeResponse.builder()
                .id(1L)
                .titulo("Filme 1")
                .poster("poster1.jpg")
                .backdrop("backdrop1.jpg")
                .classificacao("14+")
                .duracao(120)
                .generos(List.of("Ação"))
                .diretores(List.of("Diretor 1"))
                .sinopse("Sinopse 1")
                .elenco(List.of("Ator 1"))
                .status(StatusFilme.EM_CARTAZ)
                .mediaAvaliacao(4.5)
                .qtdAvaliacoes(10)
                .build();

        when(filmeService.listarTodos()).thenReturn(List.of(f1));

        ResponseEntity<List<FilmeResponse>> response = filmeController.listarTodos();

        verify(filmeService).listarTodos();
        assert response.getBody() != null;
        assertEquals(1, response.getBody().size());
        assertEquals(f1, response.getBody().getFirst());
    }

    @Test
    void buscarPorId_shouldReturnSingleFilmeFromService() {
        FilmeResponse f = FilmeResponse.builder()
                .id(2L)
                .titulo("Filme 2")
                .poster("poster2.jpg")
                .backdrop("backdrop2.jpg")
                .classificacao("18+")
                .duracao(140)
                .generos(List.of("Drama"))
                .diretores(List.of("Diretor 2"))
                .sinopse("Sinopse 2")
                .elenco(List.of("Ator 2"))
                .status(StatusFilme.EM_CARTAZ)
                .mediaAvaliacao(4.0)
                .qtdAvaliacoes(5)
                .build();

        when(filmeService.buscarPorId(2L)).thenReturn(f);

        ResponseEntity<FilmeResponse> response = filmeController.buscarPorId(2L);

        verify(filmeService).buscarPorId(2L);
        assertEquals(f, response.getBody());
    }
}