package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.AssentoService;
import com.es.cinema.tickets.application.service.SessaoService;
import com.es.cinema.tickets.web.dto.request.SessaoRequest;
import com.es.cinema.tickets.web.dto.response.AssentoResponse;
import com.es.cinema.tickets.web.dto.response.SessaoAssentosResponse;
import com.es.cinema.tickets.web.dto.response.SessaoResponse;
import com.es.cinema.tickets.persistence.enums.StatusAssento;
import com.es.cinema.tickets.persistence.enums.TipoAssento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessaoControllerTest {

    private SessaoService sessaoService;
    private AssentoService assentoService;
    private SessaoController sessaoController;

    @BeforeEach
    void setup() {
        sessaoService = Mockito.mock(SessaoService.class);
        assentoService = Mockito.mock(AssentoService.class);
        sessaoController = new SessaoController(sessaoService, assentoService);
    }

    @Test
    void criar_deveRetornarSessaoResponseComStatusCreated() {
        SessaoRequest request;
        // preencher campos necessários
        request = new SessaoRequest();
        request.getClass().getDeclaredFields()[0].setAccessible(true); // workaround reflection
        // podemos ignorar preenchimento detalhado, foco no teste unitário

        SessaoResponse mockResponse = SessaoResponse.builder()
                .id(1L)
                .filmeId(100L)
                .salaId(10L)
                .inicio(LocalDateTime.now().plusDays(1))
                .tipo("3D")
                .build();

        when(sessaoService.criar(request)).thenReturn(mockResponse);

        ResponseEntity<SessaoResponse> response = sessaoController.criar(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(sessaoService, times(1)).criar(request);
    }

    @Test
    void listarPorData_deveRetornarListaDeSessoes() {
        LocalDate data = LocalDate.now();
        List<SessaoResponse> mockList = List.of(
                SessaoResponse.builder().id(1L).filmeId(100L).salaId(10L).inicio(LocalDateTime.now()).tipo("3D").build()
        );

        when(sessaoService.listarPorData(data)).thenReturn(mockList);

        ResponseEntity<List<SessaoResponse>> response = sessaoController.listarPorData(data);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().getFirst().getId());

        verify(sessaoService, times(1)).listarPorData(data);
    }

    @Test
    void buscarPorId_deveRetornarSessao() {
        long id = 1L;
        SessaoResponse mockResponse = SessaoResponse.builder()
                .id(id)
                .filmeId(100L)
                .salaId(10L)
                .inicio(LocalDateTime.now())
                .tipo("3D")
                .build();

        when(sessaoService.buscarPorId(id)).thenReturn(mockResponse);

        ResponseEntity<SessaoResponse> response = sessaoController.buscarPorId(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(id, response.getBody().getId());

        verify(sessaoService, times(1)).buscarPorId(id);
    }

    @Test
    void listarAssentos_deveRetornarAssentosDaSessao() {
        long sessaoId = 1L;

        List<AssentoResponse> assentosMock = List.of(
                AssentoResponse.builder().id(1L).codigo("A1").status(StatusAssento.DISPONIVEL).tipo(TipoAssento.COMUM).valor(BigDecimal.TEN).build()
        );

        SessaoAssentosResponse mockResponse = SessaoAssentosResponse.builder()
                .sessaoId(sessaoId)
                .assentos(assentosMock)
                .build();

        when(assentoService.listarPorSessao(sessaoId)).thenReturn(mockResponse);

        ResponseEntity<SessaoAssentosResponse> response = sessaoController.listarAssentos(sessaoId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(sessaoId, response.getBody().getSessaoId());
        assertEquals(1, response.getBody().getAssentos().size());

        verify(assentoService, times(1)).listarPorSessao(sessaoId);
    }
}