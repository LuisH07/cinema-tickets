package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.NotificacaoService;
import com.es.cinema.tickets.persistence.entity.Notificacao;
import com.es.cinema.tickets.security.AuthUserDetails;
import com.es.cinema.tickets.web.dto.request.NotificacaoRequest;
import com.es.cinema.tickets.web.dto.response.NotificacaoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacaoControllerTest {

    private NotificacaoService service;
    private NotificacaoController controller;

    @BeforeEach
    void setup() {
        service = Mockito.mock(NotificacaoService.class);
        controller = new NotificacaoController(service);
    }

    @Test
    void agendar_deveRetornarNotificacaoResponse() {
        // mock do usuário logado
        AuthUserDetails usuario = mock(AuthUserDetails.class);
        when(usuario.getId()).thenReturn(1L);

        // mock da requisição
        NotificacaoRequest request = new NotificacaoRequest(
                List.of(1L, 2L),
                15,
                100L,
                "token-abc"
        );

        // mock da resposta do serviço
        NotificacaoResponse responseMock = new NotificacaoResponse(
                "AGENDADA",
                LocalDateTime.now().plusMinutes(15),
                "Notificação agendada com sucesso"
        );

        when(service.agendar(request, 1L)).thenReturn(responseMock);

        // chama o controller
        ResponseEntity<NotificacaoResponse> responseEntity = controller.agendar(request, usuario);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals("AGENDADA", responseEntity.getBody().status());
        assertEquals("Notificação agendada com sucesso", responseEntity.getBody().mensagem());

        verify(service, times(1)).agendar(request, 1L);
    }

    @Test
    void listarPorUsuario_deveRetornarLista() {
        AuthUserDetails usuario = mock(AuthUserDetails.class);
        when(usuario.getId()).thenReturn(1L);

        List<Notificacao> notificacoesMock = List.of(
                new Notificacao(), new Notificacao()
        );

        when(service.listarPorUsuario(1L)).thenReturn(notificacoesMock);

        ResponseEntity<List<Notificacao>> responseEntity = controller.listarPorUsuario(usuario);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
        assert responseEntity.getBody() != null;
        assertEquals(2, responseEntity.getBody().size());

        verify(service, times(1)).listarPorUsuario(1L);
    }

    @Test
    void alternarVisto_deveChamarServico() {
        Long notificacaoId = 10L;

        // apenas verifica se o método do serviço foi chamado
        ResponseEntity<Void> responseEntity = controller.alternarVisto(notificacaoId);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNull(responseEntity.getBody());

        verify(service, times(1)).alternarVisto(notificacaoId);
    }
}