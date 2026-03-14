package com.es.cinema.tickets.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class IngressoDetalheResponse {

    @JsonProperty("ingresso_id")
    private String ingressoId;

    private String status;

    @JsonProperty("codigo_autenticacao")
    private String codigoAutenticacao;

    private ClienteResponse cliente;

    @JsonProperty("detalhes_sessao")
    private DetalhesSessaoResponse detalhesSessao;

    @Getter
    @Builder
    public static class ClienteResponse {
        private String nome;
        private String cpf;
    }

    @Getter
    @Builder
    public static class DetalhesSessaoResponse {
        private String filme;
        private String data;
        private String horario;
        private String sala;
        private List<String> assentos;
    }
}
