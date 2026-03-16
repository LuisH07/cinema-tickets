package com.es.cinema.tickets.web.mapper;

import com.es.cinema.tickets.persistence.entity.*;
import com.es.cinema.tickets.persistence.enums.*;
import com.es.cinema.tickets.web.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IngressoMapperTest {

    private IngressoMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new IngressoMapper();
    }

    @Test
    void testToDetalheResponse() {
        Filme filme = Filme.builder()
                .id(1L)
                .titulo("Matrix")
                .status(StatusFilme.EM_CARTAZ)
                .build();

        Sala sala = Sala.builder()
                .id(1L)
                .nome("Sala 1")
                .capacidade(100)
                .build();

        Sessao sessao = Sessao.builder()
                .id(1L)
                .filme(filme)
                .sala(sala)
                .inicio(LocalDateTime.of(2026,3,16,20,0))
                .tipo("3D")
                .build();

        AssentoSessao a1 = AssentoSessao.builder().id(1L).codigo("A1").build();
        AssentoSessao a2 = AssentoSessao.builder().id(2L).codigo("B2").build();

        User user = User.builder()
                .id(1L)
                .nome("João")
                .cpf("12345678901")
                .email("joao@email.com")
                .passwordHash("hash123")
                .role(Role.USER)
                .build();

        Pedido pedido = Pedido.builder()
                .id(1L)
                .sessao(sessao)
                .user(user)
                .valorTotal(new BigDecimal("50.00"))
                .assentos(Set.of(a1, a2))
                .ingressosIds(List.of("I1", "I2"))
                .build();

        Ingresso ingresso = Ingresso.builder()
                .id(1L)
                .codigo("I1")
                .codigoAutenticacao("ABC123")
                .pedido(pedido)
                .usuario(user)
                .status(StatusIngresso.CONFIRMADO)
                .build();

        IngressoDetalheResponse detalhe = mapper.toDetalheResponse(ingresso);

        assertEquals("I1", detalhe.getIngressoId());
        assertEquals("confirmado", detalhe.getStatus());
        assertEquals("ABC123", detalhe.getCodigoAutenticacao());
        assertEquals("João", detalhe.getCliente().getNome());
        assertEquals("12345678901", detalhe.getCliente().getCpf());
        assertEquals("Matrix", detalhe.getDetalhesSessao().getFilme());
        assertEquals("Sala 1", detalhe.getDetalhesSessao().getSala());
        assertTrue(detalhe.getDetalhesSessao().getAssentos().containsAll(List.of("A1","B2")));
    }

    @Test
    void testToResumoResponse() {
        Filme filme = Filme.builder()
                .id(1L)
                .titulo("Matrix")
                .status(StatusFilme.EM_CARTAZ)
                .build();

        Sala sala = Sala.builder()
                .id(1L)
                .nome("Sala 1")
                .capacidade(100)
                .build();

        Sessao sessao = Sessao.builder()
                .id(1L)
                .filme(filme)
                .sala(sala)
                .inicio(LocalDateTime.of(2026,3,16,20,0))
                .tipo("3D")
                .build();

        AssentoSessao a1 = AssentoSessao.builder().id(1L).codigo("A1").build();
        AssentoSessao a2 = AssentoSessao.builder().id(2L).codigo("B2").build();

        User user = User.builder()
                .id(1L)
                .nome("João")
                .cpf("12345678901")
                .email("joao@email.com")
                .passwordHash("hash123")
                .role(Role.USER)
                .build();

        Pedido pedido = Pedido.builder()
                .id(1L)
                .sessao(sessao)
                .user(user)
                .valorTotal(new BigDecimal("50.00"))
                .assentos(Set.of(a1, a2))
                .ingressosIds(List.of("I1", "I2"))
                .build();

        Ingresso ingresso = Ingresso.builder()
                .id(1L)
                .codigo("I1")
                .codigoAutenticacao("ABC123")
                .pedido(pedido)
                .usuario(user)
                .status(StatusIngresso.CONFIRMADO)
                .build();

        IngressoResumoResponse resumo = mapper.toResumoResponse(ingresso);

        assertEquals("I1", resumo.getId());
        assertEquals("Matrix", resumo.getFilme());
        assertEquals("Sala 1", resumo.getSala());
        assertTrue(resumo.getAssentos().containsAll(List.of("A1","B2")));
        assertEquals("confirmado", resumo.getStatus());
    }

    @Test
    void testToResumoList() {
        Filme filme = Filme.builder()
                .id(1L)
                .titulo("Matrix")
                .status(StatusFilme.EM_CARTAZ)
                .build();

        Sala sala = Sala.builder()
                .id(1L)
                .nome("Sala 1")
                .capacidade(100)
                .build();

        Sessao sessao = Sessao.builder()
                .id(1L)
                .filme(filme)
                .sala(sala)
                .inicio(LocalDateTime.of(2026, 3, 16, 20, 0)) // importante!
                .tipo("3D")
                .build();

        User user = User.builder()
                .id(1L)
                .nome("João")
                .cpf("12345678901")
                .email("joao@email.com")
                .passwordHash("hash123")
                .role(Role.USER)
                .build();

        Pedido pedido = Pedido.builder()
                .id(1L)
                .sessao(sessao)
                .user(user)
                .assentos(Set.of()) // pode ser vazio
                .ingressosIds(List.of())
                .valorTotal(BigDecimal.valueOf(50))
                .metodo(MetodoPagamento.CARTAO_CREDITO)
                .build();

        Ingresso i1 = Ingresso.builder().codigo("I1").pedido(pedido).usuario(user).status(StatusIngresso.CONFIRMADO).build();
        Ingresso i2 = Ingresso.builder().codigo("I2").pedido(pedido).usuario(user).status(StatusIngresso.CONFIRMADO).build();

        List<IngressoResumoResponse> list = mapper.toResumoList(List.of(i1,i2));
        assertEquals(2, list.size());
        assertEquals("I1", list.get(0).getId());
        assertEquals("I2", list.get(1).getId());
    }
}