package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.business.*;
import com.es.cinema.tickets.exception.notfound.IngressoNotFoundException;
import com.es.cinema.tickets.persistence.entity.*;
import com.es.cinema.tickets.persistence.enums.StatusIngresso;
import com.es.cinema.tickets.persistence.repository.FilmeRepository;
import com.es.cinema.tickets.persistence.repository.IngressoRepository;
import com.es.cinema.tickets.web.dto.request.AvaliacaoRequest;
import com.es.cinema.tickets.web.dto.response.AvaliacaoRegistradaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvaliacaoServiceTest {

    private IngressoRepository ingressoRepository;
    private FilmeRepository filmeRepository;
    private AvaliacaoService avaliacaoService;

    private User usuario;
    private Filme filme;
    private Pedido pedido;
    private Ingresso ingresso;

    @BeforeEach
    void setUp() {
        ingressoRepository = mock(IngressoRepository.class);
        filmeRepository = mock(FilmeRepository.class);

        avaliacaoService = new AvaliacaoService(ingressoRepository, filmeRepository);

        usuario = User.builder()
                .id(1L)
                .email("user@test.com")
                .nome("Test User")
                .cpf("12345678901")
                .passwordHash("encoded")
                .role(com.es.cinema.tickets.persistence.enums.Role.USER)
                .build();

        filme = Filme.builder()
                .id(1L)
                .titulo("Test Movie")
                .poster("poster.png")
                .backdrop("backdrop.png")
                .classificacao("L")
                .duracao(120)
                .sinopse("Sinopse")
                .status(com.es.cinema.tickets.persistence.enums.StatusFilme.EM_CARTAZ)
                .build();

        pedido = Pedido.builder()
                .id(1L)
                .sessao(Sessao.builder().id(1L).inicio(LocalDateTime.now().minusHours(1)).filme(filme).build())
                .build();

        ingresso = Ingresso.builder()
                .id(1L)
                .codigo("ABC123")
                .codigoAutenticacao("XYZ456")
                .status(StatusIngresso.UTILIZADO)
                .usuario(usuario)
                .pedido(pedido)
                .build();
    }

    @Test
    void registrar_shouldThrow_whenIngressoNotFound() {
        AvaliacaoRequest request = new AvaliacaoRequest("ABC123", 5);
        when(ingressoRepository.findByCodigoComDetalhes("ABC123")).thenReturn(Optional.empty());

        Long usuarioId = usuario.getId();

        assertThrows(IngressoNotFoundException.class,
                () -> avaliacaoService.registrar(request, usuarioId));
    }

    @Test
    void registrar_shouldThrow_whenUsuarioNaoEhDono() {
        AvaliacaoRequest request = new AvaliacaoRequest("ABC123", 5);
        when(ingressoRepository.findByCodigoComDetalhes("ABC123")).thenReturn(Optional.of(ingresso));

        assertThrows(IngressoAcessoNegadoException.class,
                () -> avaliacaoService.registrar(request, 999L));
    }

    @Test
    void registrar_shouldThrow_whenIngressoNaoUtilizado() {
        AvaliacaoRequest request = new AvaliacaoRequest("ABC123", 5);
        ingresso.setStatus(StatusIngresso.CONFIRMADO);
        when(ingressoRepository.findByCodigoComDetalhes("ABC123")).thenReturn(Optional.of(ingresso));

        Long usuarioId = usuario.getId();

        assertThrows(IngressoNaoUtilizadoException.class,
                () -> avaliacaoService.registrar(request, usuarioId));
    }

    @Test
    void registrar_shouldThrow_whenIngressoJaAvaliado() {
        AvaliacaoRequest request = new AvaliacaoRequest("ABC123", 5);
        ingresso.setStatus(StatusIngresso.AVALIADO);
        when(ingressoRepository.findByCodigoComDetalhes("ABC123")).thenReturn(Optional.of(ingresso));

        Long usuarioId = usuario.getId();

        assertThrows(IngressoJaAvaliadoException.class,
                () -> avaliacaoService.registrar(request, usuarioId));
    }

    @Test
    void registrar_shouldThrow_whenSessaoNaoEncerrada() {
        AvaliacaoRequest request = new AvaliacaoRequest("ABC123", 5);
        pedido.getSessao().setInicio(LocalDateTime.now().plusHours(1));
        ingresso.setStatus(StatusIngresso.UTILIZADO);
        when(ingressoRepository.findByCodigoComDetalhes("ABC123")).thenReturn(Optional.of(ingresso));

        Long usuarioId = usuario.getId();

        assertThrows(IngressoNaoUtilizadoException.class,
                () -> avaliacaoService.registrar(request, usuarioId));
    }

    @Test
    void registrar_shouldRegisterAvaliacao_whenAllValid() {
        AvaliacaoRequest request = new AvaliacaoRequest("ABC123", 5);
        ingresso.setStatus(StatusIngresso.UTILIZADO);
        when(ingressoRepository.findByCodigoComDetalhes("ABC123")).thenReturn(Optional.of(ingresso));

        AvaliacaoRegistradaResponse response = avaliacaoService.registrar(request, usuario.getId());

        assertEquals(ingresso.getId(), response.getId());
        assertEquals(5, response.getNota());
        assertEquals("ABC123", response.getIngressoId());
        assertEquals(filme.getTitulo(), response.getFilmeTitulo());
        assertEquals(1, filme.getQtdAvaliacoes());
        assertEquals(5.0, filme.getMediaAvaliacao());
        assertEquals(StatusIngresso.AVALIADO, ingresso.getStatus());

        verify(filmeRepository).save(filme);
        verify(ingressoRepository).save(ingresso);
    }
}