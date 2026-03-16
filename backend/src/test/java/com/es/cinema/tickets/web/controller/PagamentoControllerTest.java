package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.PagamentoService;
import com.es.cinema.tickets.security.AuthUserDetails;
import com.es.cinema.tickets.web.dto.request.PagamentoRequest;
import com.es.cinema.tickets.web.dto.response.PagamentoResponse;
import com.es.cinema.tickets.persistence.enums.MetodoPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagamentoControllerTest {

    private PagamentoService pagamentoService;
    private PagamentoController pagamentoController;

    @BeforeEach
    void setup() {
        pagamentoService = Mockito.mock(PagamentoService.class);
        pagamentoController = new PagamentoController(pagamentoService);
    }

    @Test
    void processar_deveRetornarPagamentoResponse() {
        // mock do usuário logado
        AuthUserDetails usuario = mock(AuthUserDetails.class);
        when(usuario.getId()).thenReturn(1L);

        // mock da requisição
        PagamentoRequest request = new PagamentoRequest(
                100L,
                List.of(1L, 2L),
                new BigDecimal("50.0"),
                MetodoPagamento.CARTAO_CREDITO,
                "token-abc"
        );

        // mock da resposta do serviço
        PagamentoResponse responseMock = PagamentoResponse.builder()
                .status("APROVADO")
                .mensagem("Pagamento processado com sucesso")
                .ingressosIds(List.of("ABC123", "DEF456"))
                .ingressoCodigo("ABC123")
                .build();

        when(pagamentoService.processar(request, 1L)).thenReturn(responseMock);

        // chama o controller
        ResponseEntity<PagamentoResponse> responseEntity = pagamentoController.processar(request, usuario);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals("APROVADO", responseEntity.getBody().getStatus());
        assertEquals("Pagamento processado com sucesso", responseEntity.getBody().getMensagem());
        assertEquals(2, responseEntity.getBody().getIngressosIds().size());
        assertEquals("ABC123", responseEntity.getBody().getIngressoCodigo());

        // verifica se o serviço foi chamado corretamente
        verify(pagamentoService, times(1)).processar(request, 1L);
    }
}