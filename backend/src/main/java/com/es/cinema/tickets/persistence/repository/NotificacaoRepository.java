package com.es.cinema.tickets.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.es.cinema.tickets.persistence.entity.Notificacao;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByEnviadoFalseAndDataEnvioAgendadaBefore(LocalDateTime data);

    List<Notificacao> findByDeviceTokenOrderByDataEnvioAgendadaDesc(String deviceToken);
}
