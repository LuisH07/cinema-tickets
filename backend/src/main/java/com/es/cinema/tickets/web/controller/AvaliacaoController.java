package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.AvaliacaoService;
import com.es.cinema.tickets.security.AuthUserDetails;
import com.es.cinema.tickets.web.dto.request.AvaliacaoRequest;
import com.es.cinema.tickets.web.dto.response.AvaliacaoRegistradaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoRegistradaResponse> registrar(
            @Valid @RequestBody AvaliacaoRequest request,
            @AuthenticationPrincipal AuthUserDetails userDetails
    ) {
        AvaliacaoRegistradaResponse response = avaliacaoService.registrar(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
