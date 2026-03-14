package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    Optional<Ingresso> findByCodigo(String codigo);

    @Query("""
            SELECT i FROM Ingresso i
              JOIN FETCH i.pedido p
              JOIN FETCH p.sessao s
              JOIN FETCH s.filme
              JOIN FETCH s.sala
              JOIN FETCH p.assentos
              JOIN FETCH i.usuario
            WHERE i.codigo = :codigo
            """)
    Optional<Ingresso> findByCodigoComDetalhes(@Param("codigo") String codigo);

    @Query("""
            SELECT i FROM Ingresso i
              JOIN FETCH i.pedido p
              JOIN FETCH p.sessao s
              JOIN FETCH s.filme
              JOIN FETCH s.sala
              JOIN FETCH p.assentos
              JOIN FETCH i.usuario
            WHERE i.codigoAutenticacao = :codigoAutenticacao
            """)
    Optional<Ingresso> findByCodigoAutenticacaoComDetalhes(@Param("codigoAutenticacao") String codigoAutenticacao);

    @Query("""
            SELECT i FROM Ingresso i
              JOIN FETCH i.pedido p
              JOIN FETCH p.sessao s
              JOIN FETCH s.filme
              JOIN FETCH s.sala
              JOIN FETCH p.assentos
            WHERE i.usuario.id = :usuarioId
            ORDER BY i.criadoEm DESC
            """)
    List<Ingresso> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);
}
