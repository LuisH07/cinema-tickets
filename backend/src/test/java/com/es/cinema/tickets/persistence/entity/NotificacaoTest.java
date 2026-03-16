package com.es.cinema.tickets.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificacaoTest {

    @Test
    void testGettersSettersAndDefaults() {
        LocalDateTime agora = LocalDateTime.now();

        Notificacao notificacao = Notificacao.builder()
                .id(1L)
                .usuarioId(10L)
                .deviceToken("token123")
                .mensagem("Mensagem teste")
                .dataEnvioAgendada(agora)
                .enviado(false)
                .sessaoId(100L)
                .tituloFilme("Filme X")
                .sala("Sala 1")
                .horario("19:30")
                .visto(false)
                .build();

        // Getters
        assertEquals(1L, notificacao.getId());
        assertEquals(10L, notificacao.getUsuarioId());
        assertEquals("token123", notificacao.getDeviceToken());
        assertEquals("Mensagem teste", notificacao.getMensagem());
        assertEquals(agora, notificacao.getDataEnvioAgendada());
        assertFalse(notificacao.isEnviado());
        assertEquals(100L, notificacao.getSessaoId());
        assertEquals("Filme X", notificacao.getTituloFilme());
        assertEquals("Sala 1", notificacao.getSala());
        assertEquals("19:30", notificacao.getHorario());
        assertFalse(notificacao.isVisto());

        // Setters
        notificacao.setEnviado(true);
        notificacao.setVisto(true);
        assertTrue(notificacao.isEnviado());
        assertTrue(notificacao.isVisto());

        // toString
        String str = notificacao.toString();
        assertTrue(str.contains("Notificacao"));
        assertTrue(str.contains("dataEnvio"));
        assertTrue(str.contains("enviado"));

        // equals e hashCode
        Notificacao outra = Notificacao.builder().id(1L).build();
        assertEquals(notificacao, outra);
        assertEquals(notificacao.hashCode(), outra.hashCode());

        Notificacao diferente = Notificacao.builder().id(2L).build();
        assertNotEquals(notificacao, diferente);
    }
}