package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.MetodoPagamento;
import com.es.cinema.tickets.persistence.enums.StatusPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void testPedidoEntity() {
        Sessao sessao = new Sessao();
        User user = new User();

        Pedido pedido = Pedido.builder()
                .id(1L)
                .sessao(sessao)
                .user(user)
                .valorTotal(new BigDecimal("100.50"))
                .metodo(MetodoPagamento.CARTAO_CREDITO)
                .status(StatusPedido.PENDENTE)
                .ingressosIds(new ArrayList<>(List.of("A1", "A2"))) // <- lista mutável
                .criadoEm(LocalDateTime.now())
                .build();

        // Getters
        assertEquals(1L, pedido.getId());
        assertEquals(sessao, pedido.getSessao());
        assertEquals(user, pedido.getUser());
        assertEquals(new BigDecimal("100.50"), pedido.getValorTotal());
        assertEquals(MetodoPagamento.CARTAO_CREDITO, pedido.getMetodo());
        assertEquals(StatusPedido.PENDENTE, pedido.getStatus());
        assertTrue(pedido.getIngressosIds().contains("A1"));
        assertTrue(pedido.getIngressosIds().contains("A2"));
        assertNotNull(pedido.getCriadoEm());

        // Setters
        pedido.setStatus(StatusPedido.CANCELADO);
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());

        pedido.setValorTotal(new BigDecimal("200"));
        assertEquals(new BigDecimal("200"), pedido.getValorTotal());

        // Métodos de negócio
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.aprovar(List.of("B1", "B2"));
        assertEquals(StatusPedido.PAGO, pedido.getStatus());
        assertTrue(pedido.getIngressosIds().containsAll(List.of("A1", "A2", "B1", "B2")));

        pedido.cancelar();
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());

        // toString
        String str = pedido.toString();
        assertTrue(str.contains("Pedido"));
        assertTrue(str.contains("status"));
        assertTrue(str.contains("ingressosIds"));

        // equals e hashCode
        Pedido outro = Pedido.builder().id(1L).build();
        assertEquals(pedido, outro);
        assertEquals(pedido.hashCode(), outro.hashCode());

        Pedido diferente = Pedido.builder().id(2L).build();
        assertNotEquals(pedido, diferente);
    }
}