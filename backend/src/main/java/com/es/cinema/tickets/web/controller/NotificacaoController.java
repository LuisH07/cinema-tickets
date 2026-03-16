package com.es.cinema.tickets.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.es.cinema.tickets.application.service.NotificacaoService;
import com.es.cinema.tickets.persistence.entity.Notificacao;
import com.es.cinema.tickets.web.dto.request.NotificacaoRequest;
import com.es.cinema.tickets.web.dto.response.NotificacaoResponse;
import com.es.cinema.tickets.security.AuthUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService service;

    @PostMapping("/agendar")
    public ResponseEntity<NotificacaoResponse> agendar(
            @RequestBody NotificacaoRequest request,
            @AuthenticationPrincipal AuthUserDetails usuarioLogado) { 
        
        NotificacaoResponse response = service.agendar(request, usuarioLogado.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping 
    public ResponseEntity<List<Notificacao>> listarPorUsuario(
            @AuthenticationPrincipal AuthUserDetails usuarioLogado) { 
        
        return ResponseEntity.ok(service.listarPorUsuario(usuarioLogado.getId()));
    }

    @PatchMapping("/{id}/visto")
    public ResponseEntity<Void> alternarVisto(@PathVariable Long id) {
        service.alternarVisto(id);
        return ResponseEntity.ok().build();
    }
}