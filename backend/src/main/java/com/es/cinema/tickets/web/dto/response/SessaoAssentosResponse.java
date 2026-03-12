package com.es.cinema.tickets.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessaoAssentosResponse {
    private Long sessaoId;
    private List<AssentoResponse> assentos;
}
