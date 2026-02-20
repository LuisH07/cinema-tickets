package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.SessaoService;
import com.es.cinema.tickets.web.dto.request.SessaoRequest;
import com.es.cinema.tickets.web.dto.response.SessaoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService sessaoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SessaoResponse> criar(
            @Valid @RequestBody SessaoRequest request
    ) {
        SessaoResponse response = sessaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SessaoResponse>> listarPorData(
            @RequestParam LocalDate data
    ) {
        return ResponseEntity.ok(sessaoService.listarPorData(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sessaoService.buscarPorId(id));
    }
}