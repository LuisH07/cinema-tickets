package com.es.cinema.tickets.web.mapper;

import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.web.dto.response.AssentoResponse;
import com.es.cinema.tickets.web.dto.response.SessaoAssentosResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssentoMapper {

    public AssentoResponse toResponse(AssentoSessao assento) {
        return AssentoResponse.builder()
                .id(assento.getId())
                .codigo(assento.getCodigo())
                .status(assento.getStatus())
                .tipo(assento.getTipo())
                .valor(assento.getValor())
                .build();
    }

    public SessaoAssentosResponse toSessaoAssentosResponse(Long sessaoId, List<AssentoSessao> assentos) {
        return SessaoAssentosResponse.builder()
                .sessaoId(sessaoId)
                .assentos(assentos.stream().map(this::toResponse).toList())
                .build();
    }
}
