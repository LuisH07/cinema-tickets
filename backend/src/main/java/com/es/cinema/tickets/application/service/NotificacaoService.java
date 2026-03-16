package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.persistence.entity.Notificacao;
import com.es.cinema.tickets.persistence.entity.Sessao; 
import com.es.cinema.tickets.persistence.repository.NotificacaoRepository;
import com.es.cinema.tickets.persistence.repository.SessaoRepository;
import com.es.cinema.tickets.web.dto.request.NotificacaoRequest;
import com.es.cinema.tickets.web.dto.response.NotificacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional(readOnly = true)
    public List<Notificacao> listarPorToken(String deviceToken) {
        LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        
        // Retorna apenas notificações com a data/hora agendada no passado ou no exato momento atual
        return repository.findByDeviceTokenOrderByDataEnvioAgendadaDesc(deviceToken)
                .stream()
                .filter(n -> !n.getDataEnvioAgendada().isAfter(agora))
                .toList();
    }

    @Transactional
    public void alternarVisto(Long id) {
        Notificacao n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada com ID: " + id));
        n.setVisto(!n.isVisto()); 
        repository.save(n);
    }
}