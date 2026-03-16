package com.es.cinema.tickets.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoIngressoRequest {

    @NotBlank(message = "O código do voucher é obrigatório")
    @JsonProperty("codigo_voucher")
    private String codigoVoucher;
}
