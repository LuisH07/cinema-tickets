package com.es.cinema.tickets.web.mapper;

import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.persistence.entity.Ingresso;
import com.es.cinema.tickets.persistence.entity.Pedido;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.web.dto.response.IngressoDetalheResponse;
import com.es.cinema.tickets.web.dto.response.IngressoResumoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IngressoMapper {

    public IngressoDetalheResponse toDetalheResponse(Ingresso ingresso) {
        Pedido pedido = ingresso.getPedido();
        Sessao sessao = pedido.getSessao();

        List<String> codigosAssentos = pedido.getAssentos().stream()
                .map(AssentoSessao::getCodigo)
                .sorted()
                .toList();

        return IngressoDetalheResponse.builder()
                .ingressoId(ingresso.getCodigo())
                .status(ingresso.getStatus().name().toLowerCase())
                .codigoAutenticacao(ingresso.getCodigoAutenticacao())
                .cliente(IngressoDetalheResponse.ClienteResponse.builder()
                        .nome(ingresso.getUsuario().getNome())
                        .cpf(ingresso.getUsuario().getCpf())
                        .build())
                .detalhesSessao(IngressoDetalheResponse.DetalhesSessaoResponse.builder()
                        .filme(sessao.getFilme().getTitulo())
                        .data(sessao.getInicio().toLocalDate().toString())
                        .horario(sessao.getInicio().toLocalTime().toString())
                        .sala(sessao.getSala().getNome())
                        .assentos(codigosAssentos)
                        .build())
                .build();
    }

    public IngressoResumoResponse toResumoResponse(Ingresso ingresso) {
        Pedido pedido = ingresso.getPedido();
        Sessao sessao = pedido.getSessao();

        List<String> codigosAssentos = pedido.getAssentos().stream()
                .map(AssentoSessao::getCodigo)
                .sorted()
                .toList();

        return IngressoResumoResponse.builder()
                .id(ingresso.getCodigo())
                .filme(sessao.getFilme().getTitulo())
                .data(sessao.getInicio().toLocalDate().toString())
                .horario(sessao.getInicio().toLocalTime().toString())
                .sala(sessao.getSala().getNome())
                .assentos(codigosAssentos)
                .status(ingresso.getStatus().name().toLowerCase())
                .build();
    }

    public List<IngressoResumoResponse> toResumoList(List<Ingresso> ingressos) {
        return ingressos.stream()
                .map(this::toResumoResponse)
                .toList();
    }
}
