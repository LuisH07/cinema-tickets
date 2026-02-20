package com.es.cinema.tickets.web.mapper;

import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.web.dto.request.SessaoRequest;
import com.es.cinema.tickets.web.dto.response.SessaoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessaoMapper {

    public Sessao toEntity(SessaoRequest request, Filme filme, Sala sala) {
        return Sessao.builder()
                .filme(filme)
                .sala(sala)
                .inicio(request.getInicio())
                .tipo(request.getTipo())
                .build();
    }

    public SessaoResponse toResponse(Sessao sessao) {
        return SessaoResponse.builder()
                .id(sessao.getId())
                .filmeId(sessao.getFilme().getId())
                .salaId(sessao.getSala().getId())
                .inicio(sessao.getInicio())
                .tipo(sessao.getTipo())
                .build();
    }

    public List<SessaoResponse> toResponseList(List<Sessao> sessoes) {
        return sessoes.stream()
                .map(this::toResponse)
                .toList();
    }
}

