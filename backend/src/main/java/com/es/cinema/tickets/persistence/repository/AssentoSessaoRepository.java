package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssentoSessaoRepository extends JpaRepository<AssentoSessao, Long> {

    List<AssentoSessao> findBySessaoIdOrderByCodigoAsc(Long sessaoId);
    
    // Busca os assentos com bloqueio pessimista de escrita
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AssentoSessao a WHERE a.id IN :ids")
    List<AssentoSessao> findAllByIdWithLock(@Param("ids") List<Long> ids);
}
