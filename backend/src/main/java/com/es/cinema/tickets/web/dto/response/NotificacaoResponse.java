package com.es.cinema.tickets.web.dto.response;

import java.time.LocalDateTime;

public record NotificacaoResponse(
    String status,
    LocalDateTime dataAgendada,
    String mensagem
) {}
