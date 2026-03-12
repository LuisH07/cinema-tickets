package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.PagamentoService;
import com.es.cinema.tickets.web.dto.request.PagamentoRequest;
import com.es.cinema.tickets.web.dto.response.PagamentoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping("/processar")
    public ResponseEntity<PagamentoResponse> processar(
            @Valid @RequestBody PagamentoRequest request
    ) {
        PagamentoResponse response = pagamentoService.processar(request);
        return ResponseEntity.ok(response);
    }
}
