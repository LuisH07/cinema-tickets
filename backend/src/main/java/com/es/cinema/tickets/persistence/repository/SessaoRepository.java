package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.Sessao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    @EntityGraph(attributePaths = {"filme", "sala"})
    Optional<Sessao> findWithFilmeAndSalaById(Long id);

    @EntityGraph(attributePaths = {"filme", "sala"})
    List<Sessao> findByInicioBetween(LocalDateTime inicio, LocalDateTime fim);
}
