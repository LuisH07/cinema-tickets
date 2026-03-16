package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.SalaService;
import com.es.cinema.tickets.web.dto.response.SalaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaControllerTest {

    private SalaService salaService;
    private SalaController salaController;

    @BeforeEach
    void setup() {
        salaService = Mockito.mock(SalaService.class);
        salaController = new SalaController(salaService);
    }

    @Test
    void listarTodas_deveRetornarListaDeSalas() {
        // mock da lista de salas
        List<SalaResponse> salasMock = List.of(
                new SalaResponse(1L, "Sala 1", 100),
                new SalaResponse(2L, "Sala 2", 80)
        );

        when(salaService.listarTodas()).thenReturn(salasMock);

        ResponseEntity<List<SalaResponse>> response = salaController.listarTodas();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Sala 1", response.getBody().getFirst().getNome());

        verify(salaService, times(1)).listarTodas();
    }

    @Test
    void buscarPorId_deveRetornarSala() {
        long id = 1L;
        SalaResponse salaMock = new SalaResponse(id, "Sala 1", 100);

        when(salaService.buscarPorId(id)).thenReturn(salaMock);

        ResponseEntity<SalaResponse> response = salaController.buscarPorId(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("Sala 1", response.getBody().getNome());
        assertEquals(100, response.getBody().getCapacidade());

        verify(salaService, times(1)).buscarPorId(id);
    }
}