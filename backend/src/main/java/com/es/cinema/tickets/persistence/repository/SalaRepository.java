package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
}
