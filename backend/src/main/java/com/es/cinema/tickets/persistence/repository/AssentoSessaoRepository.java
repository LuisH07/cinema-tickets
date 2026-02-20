package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssentoSessaoRepository extends JpaRepository<AssentoSessao, Long> {
    List<AssentoSessao> findBySessaoIdOrderByCodigoAsc(Long sessaoId);
}
