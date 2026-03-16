package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.Notificacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class NotificacaoRepositoryTest {

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private final String deviceToken = "token123";

    @BeforeEach
    void setup() {
        LocalDateTime agora = LocalDateTime.now();

        // Notificações já enviadas
        entityManager.persist(Notificacao.builder()
                .deviceToken(deviceToken)
                .mensagem("Mensagem enviada")
                .dataEnvioAgendada(agora.minusHours(1))
                .enviado(true)
                .sessaoId(1L)
                .build());

        // Notificações pendentes
        entityManager.persist(Notificacao.builder()
                .deviceToken(deviceToken)
                .mensagem("Mensagem pendente 1")
                .dataEnvioAgendada(agora.minusMinutes(30))
                .enviado(false)
                .sessaoId(1L)
                .build());

        entityManager.persist(Notificacao.builder()
                .deviceToken(deviceToken)
                .mensagem("Mensagem pendente 2")
                .dataEnvioAgendada(agora.plusHours(1))
                .enviado(false)
                .sessaoId(2L)
                .build());

        entityManager.flush();
    }

    @Test
    void deveBuscarNotificacoesNaoEnviadasAntesDeAgora() {
        LocalDateTime agora = LocalDateTime.now();
        List<Notificacao> pendentes = repository.findByEnviadoFalseAndDataEnvioAgendadaBefore(agora);

        assertThat(pendentes).hasSize(1);
        assertThat(pendentes.getFirst().getMensagem()).isEqualTo("Mensagem pendente 1");
        assertThat(pendentes.getFirst().isEnviado()).isFalse();
    }

    @Test
    void deveBuscarNotificacoesPorDeviceTokenOrdenadas() {
        List<Notificacao> notificacoes = repository.findByDeviceTokenOrderByDataEnvioAgendadaDesc(deviceToken);

        assertThat(notificacoes).hasSize(3);
        assertThat(notificacoes.get(0).getDataEnvioAgendada()).isAfterOrEqualTo(notificacoes.get(1).getDataEnvioAgendada());
        assertThat(notificacoes.get(1).getDataEnvioAgendada()).isAfterOrEqualTo(notificacoes.get(2).getDataEnvioAgendada());
    }
}