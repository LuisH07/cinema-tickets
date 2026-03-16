package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.enums.StatusFilme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SessaoRepositoryTest {

    @Autowired
    private SessaoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Sessao sessao;
    private LocalDateTime agora;

    @BeforeEach
    void setup() {
        agora = LocalDateTime.now();

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
                .inicio(agora.plusHours(1))
                .tipo("2D")
                .build());

        entityManager.flush();
    }

    @Test
    void deveBuscarSessaoComFilmeESalaPorId() {
        Optional<Sessao> resultado = repository.findWithFilmeAndSalaById(sessao.getId());

        assertThat(resultado).isPresent();
        Sessao s = resultado.get();

        assertThat(s.getFilme()).isNotNull();
        assertThat(s.getFilme().getTitulo()).isEqualTo("Filme Teste");
        assertThat(s.getSala()).isNotNull();
        assertThat(s.getSala().getNome()).isEqualTo("Sala 1");
    }

    @Test
    void deveBuscarSessoesEntreDoisHorarios() {
        LocalDateTime inicio = agora;
        LocalDateTime fim = agora.plusHours(2);

        List<Sessao> sessoes = repository.findByInicioBetween(inicio, fim);

        assertThat(sessoes).hasSize(1);
        Sessao s = sessoes.getFirst();

        assertThat(s.getFilme()).isNotNull();
        assertThat(s.getFilme().getTitulo()).isEqualTo("Filme Teste");
        assertThat(s.getSala()).isNotNull();
        assertThat(s.getSala().getNome()).isEqualTo("Sala 1");
    }
}
