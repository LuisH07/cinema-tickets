package com.es.cinema.tickets.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvaliacaoRegistradaResponse {

    private Long id;
    private Integer nota;

    @JsonProperty("ingresso_id")
    private String ingressoId;

    @JsonProperty("filme")
    private String filmeTitulo;

    @JsonProperty("media_avaliacao")
    private Double mediaAvaliacao;

    @JsonProperty("qtd_avaliacoes")
    private Integer qtdAvaliacoes;

    private String mensagem;
}
