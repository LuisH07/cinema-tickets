package com.es.cinema.tickets.web.mapper;

import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.web.dto.response.SalaResponse;
import com.es.cinema.tickets.web.dto.response.SalaResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SalaMapper {

    // TODO implementar CRUD de salas futuramente
//    public Sala toEntity(SalaRequest request) {
//        return Sala.builder()
//                .nome(request.getNome())
//                .capacidade(request.getCapacidade())
//                .build();
//    }

    public SalaResponse toResponse(Sala sala) {
        return SalaResponse.builder()
                .id(sala.getId())
                .nome(sala.getNome())
                .capacidade(sala.getCapacidade())
                .build();
    }

    public List<SalaResponse> toResponseList(List<Sala> salas) {
        return salas.stream()
                .map(this::toResponse)
                .toList();
    }
}
