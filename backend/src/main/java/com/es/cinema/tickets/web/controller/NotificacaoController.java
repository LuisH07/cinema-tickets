package com.es.cinema.tickets.web.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.es.cinema.tickets.application.service.NotificacaoService;
import com.es.cinema.tickets.persistence.entity.Notificacao;
import com.es.cinema.tickets.web.dto.request.NotificacaoRequest;
import com.es.cinema.tickets.web.dto.response.NotificacaoResponse;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService service;

    @PostMapping("/agendar")
    public ResponseEntity<NotificacaoResponse> agendar(@RequestBody NotificacaoRequest request) {
        NotificacaoResponse response = service.agendar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{deviceToken}")
    public ResponseEntity<List<Notificacao>> listarPorToken(@PathVariable String deviceToken) {
        return ResponseEntity.ok(service.listarPorToken(deviceToken));
    }

    @PatchMapping("/{id}/visto")
    public ResponseEntity<Void> alternarVisto(@PathVariable Long id) {
        service.alternarVisto(id);
        return ResponseEntity.ok().build();
    }
}