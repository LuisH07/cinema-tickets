package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.persistence.entity.Notificacao;
import com.es.cinema.tickets.persistence.entity.Sessao; 
import com.es.cinema.tickets.persistence.repository.NotificacaoRepository;
import com.es.cinema.tickets.persistence.repository.SessaoRepository;
import com.es.cinema.tickets.web.dto.request.NotificacaoRequest;
import com.es.cinema.tickets.web.dto.response.NotificacaoResponse;
import com.google.firebase.messaging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private SessaoRepository sessaoRepository; 

    @Transactional
    public NotificacaoResponse agendar(NotificacaoRequest dto) {
        Sessao sessao = sessaoRepository.findWithFilmeAndSalaById(dto.sessaoId())
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada com ID: " + dto.sessaoId()));

        LocalDateTime horarioSessao = sessao.getInicio();
        LocalDateTime horarioNotificacao = horarioSessao.minusMinutes(dto.minutosAntecedencia());

        String tituloFilme = sessao.getFilme().getTitulo();
        String nomeSala = sessao.getSala().getNome();    
        String horarioFormatado = horarioSessao.format(DateTimeFormatter.ofPattern("HH:mm"));

        String mensagemCompleta = String.format(
            "Não esqueça! O filme '%s' começa às %s na %s.", 
            tituloFilme, horarioFormatado, nomeSala
        );

        Notificacao n = Notificacao.builder()
                .deviceToken(dto.deviceToken())
                .dataEnvioAgendada(horarioNotificacao)
                .sessaoId(dto.sessaoId())
                .mensagem(mensagemCompleta)
                .tituloFilme(tituloFilme)
                .sala(nomeSala)
                .horario(horarioFormatado)
                .visto(false)
                .enviado(false)
                .build();

        repository.save(n);

        return new NotificacaoResponse(
            "SUCESSO",
            horarioNotificacao,
            "Lembrete agendado para " + dto.minutosAntecedencia() + " minutos antes da sessão."
        );
    }

    @Scheduled(fixedRate = 60000)
    public void processarNotificacoes() {
        LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        List<Notificacao> pendentes = repository.findByEnviadoFalseAndDataEnvioAgendadaBefore(agora);

        for (Notificacao n : pendentes) {
            boolean sucesso = enviarFirebase(n);
            if (sucesso) {
                n.setEnviado(true);
                repository.save(n);
            }
        }
    }

    public boolean enviarFirebase(Notificacao n) {
        Message message = Message.builder()
            .setToken(n.getDeviceToken())
            .setNotification(Notification.builder()
                .setTitle("CineTickets - Lembrete de Sessão")
                .setBody(n.getMensagem())
                .build())
            .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            System.out.println("Push enviado para o token: " + n.getDeviceToken());
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao enviar Firebase: " + e.getMessage());
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<Notificacao> listarPorToken(String deviceToken) {
        return repository.findByDeviceTokenOrderByDataEnvioAgendadaDesc(deviceToken);
    }

    @Transactional
    public void alternarVisto(Long id) {
        Notificacao n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada com ID: " + id));
        n.setVisto(!n.isVisto()); 
        repository.save(n);
    }
}