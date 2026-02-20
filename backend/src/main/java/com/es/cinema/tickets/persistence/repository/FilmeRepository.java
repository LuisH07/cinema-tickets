package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
}