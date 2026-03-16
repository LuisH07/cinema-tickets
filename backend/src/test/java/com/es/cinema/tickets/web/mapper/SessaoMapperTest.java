package com.es.cinema.tickets.web.mapper;

import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.web.dto.request.SessaoRequest;
import com.es.cinema.tickets.web.dto.response.SessaoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessaoMapperTest {

    private SessaoMapper mapper;
    private Filme filme;
    private Sala sala;

    @BeforeEach
    void setUp() {
        mapper = new SessaoMapper();

        filme = Filme.builder()
                .id(1L)
                .titulo("Matrix")
                .status(null) // ou StatusFilme.ATIVO se você tiver enum
                .build();

        sala = Sala.builder()
                .id(1L)
                .nome("Sala 1")
                .capacidade(100)
                .build();
    }

    @Test
    void testToEntity() {
        SessaoRequest request = new SessaoRequest();
        // usando reflection ou setters já que @Getter impede set direto
        java.lang.reflect.Field inicioField;
        java.lang.reflect.Field tipoField;
        try {
            inicioField = SessaoRequest.class.getDeclaredField("inicio");
            tipoField = SessaoRequest.class.getDeclaredField("tipo");
            inicioField.setAccessible(true);
            tipoField.setAccessible(true);
            inicioField.set(request, LocalDateTime.of(2026, 3, 16, 20, 0));
            tipoField.set(request, "3D");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Sessao sessao = mapper.toEntity(request, filme, sala);
        assertEquals(filme, sessao.getFilme());
        assertEquals(sala, sessao.getSala());
        assertEquals(request.getInicio(), sessao.getInicio());
        assertEquals(request.getTipo(), sessao.getTipo());
    }

    @Test
    void testToResponse() {
        Sessao sessao = Sessao.builder()
                .id(10L)
                .filme(filme)
                .sala(sala)
                .inicio(LocalDateTime.of(2026, 3, 16, 20, 0))
                .tipo("3D")
                .build();

        SessaoResponse response = mapper.toResponse(sessao);
        assertEquals(sessao.getId(), response.getId());
        assertEquals(sessao.getFilme().getId(), response.getFilmeId());
        assertEquals(sessao.getSala().getId(), response.getSalaId());
        assertEquals(sessao.getInicio(), response.getInicio());
        assertEquals(sessao.getTipo(), response.getTipo());
    }

    @Test
    void testToResponseList() {
        Sessao sessao1 = Sessao.builder()
                .id(1L)
                .filme(filme)
                .sala(sala)
                .inicio(LocalDateTime.of(2026, 3, 16, 18, 0))
                .tipo("2D")
                .build();

        Sessao sessao2 = Sessao.builder()
                .id(2L)
                .filme(filme)
                .sala(sala)
                .inicio(LocalDateTime.of(2026, 3, 16, 20, 0))
                .tipo("3D")
                .build();

        List<SessaoResponse> responses = mapper.toResponseList(List.of(sessao1, sessao2));
        assertEquals(2, responses.size());
        assertEquals(sessao1.getId(), responses.get(0).getId());
        assertEquals(sessao2.getId(), responses.get(1).getId());
    }
}