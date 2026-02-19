package com.es.cinema.tickets.web.mapper;

import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.web.dto.request.SessaoRequestDTO;
import com.es.cinema.tickets.web.dto.response.SessaoResponseDTO;

public class SessaoMapper {

    private SessaoMapper() {
    }

    public static Sessao toEntity(SessaoRequestDTO dto, Filme filme, Sala sala) {
        return Sessao.builder()
                .data(dto.getData())
                .horario(dto.getHorario())
                .classificacao(dto.getClassificacao())
                .filme(filme)
                .sala(sala)
                .build();
    }

    public static SessaoResponseDTO toResponseDTO(Sessao sessao) {
        return SessaoResponseDTO.builder()
                .id(sessao.getId())
                .data(sessao.getData())
                .horario(sessao.getHorario())
                .classificacao(sessao.getClassificacao())
                .filmeId(sessao.getFilme().getId())
                .filmeTitulo(sessao.getFilme().getTitulo())
                .salaId(sessao.getSala().getId())
                .salaNumero(sessao.getSala().getNumero())
                .totalAssentos(sessao.getAssentos().size())
                .build();
    }
}
