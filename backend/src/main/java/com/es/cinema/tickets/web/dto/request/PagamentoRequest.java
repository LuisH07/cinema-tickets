package com.es.cinema.tickets.web.dto.request;

import com.es.cinema.tickets.persistence.enums.MetodoPagamento;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class PagamentoRequest {

    @NotNull(message = "O id da sessão é obrigatório")
    private Long sessaoId;

    @NotEmpty(message = "É necessário informar ao menos um assento")
    private List<Long> assentosIds;

    @NotNull(message = "O valor esperado é obrigatório")
    @Positive(message = "O valor esperado deve ser positivo")
    private BigDecimal valorEsperado;

    @NotNull(message = "O método de pagamento é obrigatório")
    private MetodoPagamento metodo;

    @NotNull(message = "O token de pagamento é obrigatório")
    private String tokenPagamento;
}
