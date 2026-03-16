package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.enums.StatusAssento;
import com.es.cinema.tickets.persistence.enums.StatusFilme;
import com.es.cinema.tickets.persistence.enums.TipoAssento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ActiveProfiles("test")
class AssentoSessaoRepositoryTest {

    @Autowired
    private AssentoSessaoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Sessao sessao;

    @BeforeEach
    void setupSessao() {
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

        sessao = entityManager.persist(Sessao.builder()
                .filme(filme)
                .sala(sala)
                .inicio(LocalDateTime.now().plusHours(1))
                .tipo("2D")
                .build());

        entityManager.flush();
    }

    @Test
    void deveBuscarAssentosDaSessaoOrdenadosPorCodigo() {
        AssentoSessao a2 = AssentoSessao.builder()
                .sessao(sessao)
                .codigo("B1")
                .tipo(TipoAssento.COMUM)
                .valor(BigDecimal.valueOf(20))
                .status(StatusAssento.DISPONIVEL)
                .build();

        AssentoSessao a1 = AssentoSessao.builder()
                .sessao(sessao)
                .codigo("A1")
                .tipo(TipoAssento.COMUM)
                .valor(BigDecimal.valueOf(20))
                .status(StatusAssento.DISPONIVEL)
                .build();

        entityManager.persist(a2);
        entityManager.persist(a1);
        entityManager.flush();

        List<AssentoSessao> resultado =
                repository.findBySessaoIdOrderByCodigoAsc(sessao.getId());

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("A1");
        assertThat(resultado.get(1).getCodigo()).isEqualTo("B1");
    }

    @Test
    void deveBuscarAssentosComLockPessimista() {
        AssentoSessao a1 = AssentoSessao.builder()
                .sessao(sessao)
                .codigo("A1")
                .tipo(TipoAssento.COMUM)
                .valor(BigDecimal.valueOf(20))
                .status(StatusAssento.DISPONIVEL)
                .build();

        entityManager.persist(a1);
        entityManager.flush();

        List<AssentoSessao> resultado =
                repository.findAllByIdWithLock(List.of(a1.getId()));

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getCodigo()).isEqualTo("A1");
    }
}