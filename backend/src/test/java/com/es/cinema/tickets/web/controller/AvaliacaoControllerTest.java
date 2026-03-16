package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.AvaliacaoService;
import com.es.cinema.tickets.security.AuthUserDetails;
import com.es.cinema.tickets.web.dto.request.AvaliacaoRequest;
import com.es.cinema.tickets.web.dto.response.AvaliacaoRegistradaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvaliacaoControllerTest {

    private AvaliacaoService avaliacaoService;
    private AvaliacaoController avaliacaoController;

    @BeforeEach
    void setUp() {
        avaliacaoService = mock(AvaliacaoService.class);
        avaliacaoController = new AvaliacaoController(avaliacaoService);
    }

    @Test
    void registrar_shouldCallServiceAndReturnCreated() {
        AvaliacaoRequest request = new AvaliacaoRequest("ING123", 5);

        AuthUserDetails userDetails = mock(AuthUserDetails.class);
        when(userDetails.getId()).thenReturn(42L);

        AvaliacaoRegistradaResponse responseMock = AvaliacaoRegistradaResponse.builder()
                .id(1L)
                .ingressoId("ING123")
                .nota(5)
                .filmeTitulo("Filme Teste")
                .mediaAvaliacao(5.0)
                .qtdAvaliacoes(1)
                .mensagem("Avaliação registrada com sucesso")
                .build();

        when(avaliacaoService.registrar(request, 42L)).thenReturn(responseMock);

        ResponseEntity<AvaliacaoRegistradaResponse> response =
                avaliacaoController.registrar(request, userDetails);

        verify(avaliacaoService).registrar(request, 42L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseMock, response.getBody());
    }
}