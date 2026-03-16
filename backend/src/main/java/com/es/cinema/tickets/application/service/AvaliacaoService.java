package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.business.IngressoAcessoNegadoException;
import com.es.cinema.tickets.exception.business.IngressoJaAvaliadoException;
import com.es.cinema.tickets.exception.business.IngressoNaoUtilizadoException;
import com.es.cinema.tickets.exception.notfound.IngressoNotFoundException;
import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Ingresso;
import com.es.cinema.tickets.persistence.enums.StatusIngresso;
import com.es.cinema.tickets.persistence.repository.FilmeRepository;
import com.es.cinema.tickets.persistence.repository.IngressoRepository;
import com.es.cinema.tickets.web.dto.request.AvaliacaoRequest;
import com.es.cinema.tickets.web.dto.response.AvaliacaoRegistradaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final IngressoRepository ingressoRepository;
    private final FilmeRepository filmeRepository;

    @Transactional
    public AvaliacaoRegistradaResponse registrar(AvaliacaoRequest request, Long usuarioId) {
        Ingresso ingresso = ingressoRepository.findByCodigoComDetalhes(request.getIngressoId())
                .orElseThrow(() -> new IngressoNotFoundException(request.getIngressoId()));

        // Garante que o ingresso pertence ao usuário autenticado
        if (!ingresso.getUsuario().getId().equals(usuarioId)) {
            throw new IngressoAcessoNegadoException();
        }

        // Impede avaliação duplicada
        if (StatusIngresso.AVALIADO.equals(ingresso.getStatus())) {
            throw new IngressoJaAvaliadoException();
        }

        // Ingresso deve ter sido utilizado
        if (!StatusIngresso.UTILIZADO.equals(ingresso.getStatus())) {
            throw new IngressoNaoUtilizadoException();
        }

        // Garante que a sessão já encerrou
        if (ingresso.getPedido().getSessao().getInicio().isAfter(LocalDateTime.now())) {
            throw new IngressoNaoUtilizadoException();
        }

        // Atualiza a média diretamente no Filme
        Filme filme = ingresso.getPedido().getSessao().getFilme();
        filme.registrarAvaliacao(request.getNota());
        filmeRepository.save(filme);

        // Marca o ingresso como AVALIADO
        ingresso.avaliar();
        ingressoRepository.save(ingresso);

        log.info("Avaliação registrada. filmeId={}, nota={}, novaMedia={}, usuarioId={}",
                filme.getId(), request.getNota(), filme.getMediaAvaliacao(), usuarioId);

        return AvaliacaoRegistradaResponse.builder()
                .id(ingresso.getId())
                .nota(request.getNota())
                .ingressoId(ingresso.getCodigo())
                .filmeTitulo(filme.getTitulo())
                .mediaAvaliacao(filme.getMediaAvaliacao())
                .qtdAvaliacoes(filme.getQtdAvaliacoes())
                .mensagem("Avaliação registrada com sucesso!")
                .build();
    }
}
