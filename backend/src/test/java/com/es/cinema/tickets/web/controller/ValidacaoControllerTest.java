package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.ValidacaoService;
import com.es.cinema.tickets.web.dto.request.ValidacaoIngressoRequest;
import com.es.cinema.tickets.web.dto.response.ValidacaoIngressoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidacaoControllerTest {

    private ValidacaoService validacaoService;
    private ValidacaoController validacaoController;

    @BeforeEach
    void setup() {
        validacaoService = Mockito.mock(ValidacaoService.class);
        validacaoController = new ValidacaoController(validacaoService);
    }

    @Test
    void validar_deveRetornarSucesso() {
        String codigo = "ABC123";

        // resposta mockada
        ValidacaoIngressoResponse responseMock = ValidacaoIngressoResponse.sucesso(
                "UTILIZADO", "Luis", "Filme Teste", "Sala 1", List.of("B2"), LocalDateTime.now()
        );

        when(validacaoService.validar(codigo)).thenReturn(responseMock);

        // chamando método do controller
        ValidacaoIngressoRequest request = new ValidacaoIngressoRequest(codigo);
        ResponseEntity<ValidacaoIngressoResponse> responseEntity = validacaoController.validar(request);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value()); // <-- corrige aqui
        assertNotNull(responseEntity.getBody());
        assertEquals("UTILIZADO", responseEntity.getBody().getStatus());
        assertTrue(responseEntity.getBody().isValido());
        assertEquals("Luis", responseEntity.getBody().getDadosIngresso().getCliente());

        verify(validacaoService, times(1)).validar(codigo);
    }
}