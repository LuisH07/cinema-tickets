package com.es.cinema.tickets.web.dto.request;
import java.util.List;

public record NotificacaoRequest(
    List<Long> ingressosIds,
    Integer minutosAntecedencia,
    Long sessaoId,
    String deviceToken
) {}
