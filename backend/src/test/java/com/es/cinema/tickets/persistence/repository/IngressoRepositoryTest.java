package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.*;
import com.es.cinema.tickets.persistence.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class IngressoRepositoryTest {

    @Autowired
    private IngressoRepository ingressoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User usuario;

    @BeforeEach
    void setup() {
        usuario = entityManager.persist(User.builder()
                .nome("Luis")
                .email("luis@example.com")
                .cpf("12345678901")
                .passwordHash("hash123")
                .role(Role.USER)
                .build());
        Filme filme = entityManager.persist(Filme.builder()
                .titulo("Filme Teste")
                .poster("poster.jpg")
                .backdrop("backdrop.jpg")
                .classificacao("12")
                .duracao(120)
                .sinopse("Sinopse teste")
                .status(StatusFilme.EM_CARTAZ)
                .build());
        Sala sala = entityManager.persist(Sala.builder()
                .nome("Sala 1")
                .capacidade(50)
                .build());
        Sessao sessao = entityManager.persist(Sessao.builder()
                .filme(filme)
                .sala(sala)
                .tipo("2D")
                .inicio(LocalDateTime.now())
                .build());
        AssentoSessao assento = entityManager.persist(AssentoSessao.builder()
                .sessao(sessao)
                .codigo("A1")
                .tipo(TipoAssento.COMUM)
                .valor(BigDecimal.valueOf(20))
                .status(StatusAssento.DISPONIVEL)
                .build());
        Pedido pedido = entityManager.persist(Pedido.builder()
                .sessao(sessao)
                .user(usuario)
                .assentos(Set.of(assento))
                .criadoEm(LocalDateTime.now())
                .metodo(MetodoPagamento.PIX)
                .valorTotal(BigDecimal.valueOf(20))
                .build());
        entityManager.persist(Ingresso.builder()
                .codigo("ABC123")
                .codigoAutenticacao("XYZ987")
                .pedido(pedido)
                .usuario(usuario)
                .criadoEm(LocalDateTime.now())
                .build());

        entityManager.flush();
    }

    @Test
    void deveEncontrarIngressoPorCodigo() {
        Optional<Ingresso> encontrado = ingressoRepository.findByCodigo("ABC123");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCodigo()).isEqualTo("ABC123");
    }

    @Test
    void deveBuscarIngressoComDetalhesPorCodigo() {
        Optional<Ingresso> encontrado = ingressoRepository.findByCodigoComDetalhes("ABC123");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getPedido()).isNotNull();
        assertThat(encontrado.get().getPedido().getSessao()).isNotNull();
        assertThat(encontrado.get().getPedido().getAssentos()).isNotEmpty();
        assertThat(encontrado.get().getUsuario()).isNotNull();
    }

    @Test
    void deveBuscarIngressoComDetalhesPorCodigoAutenticacao() {
        Optional<Ingresso> encontrado = ingressoRepository.findByCodigoAutenticacaoComDetalhes("XYZ987");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCodigoAutenticacao()).isEqualTo("XYZ987");
    }

    @Test
    void deveListarIngressosPorUsuario() {
        List<Ingresso> ingressos = ingressoRepository.findAllByUsuarioId(usuario.getId());
        assertThat(ingressos).isNotEmpty();
        assertThat(ingressos.getFirst().getUsuario().getNome()).isEqualTo("Luis");
    }
}