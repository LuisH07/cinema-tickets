package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.SessaoService;
import com.es.cinema.tickets.web.dto.request.SessaoRequestDTO;
import com.es.cinema.tickets.web.dto.response.SessaoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService sessaoService;

    @PostMapping
    public ResponseEntity<SessaoResponseDTO> criar(
            @Valid @RequestBody SessaoRequestDTO request
    ) {
        SessaoResponseDTO response = sessaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
