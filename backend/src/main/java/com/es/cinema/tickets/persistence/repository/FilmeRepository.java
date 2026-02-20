package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.Filme;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    @Query("SELECT DISTINCT f FROM Filme f LEFT JOIN FETCH f.generos")
    List<Filme> findAllWithGeneros();
    
    @Query("SELECT DISTINCT f FROM Filme f LEFT JOIN FETCH f.diretores")
    List<Filme> findAllWithDiretores();
    
    @Query("SELECT DISTINCT f FROM Filme f LEFT JOIN FETCH f.elenco")
    List<Filme> findAllWithElenco();
}