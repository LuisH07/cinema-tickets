package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.business.IngressoAcessoNegadoException;
import com.es.cinema.tickets.exception.business.IngressoJaAvaliadoException;
import com.es.cinema.tickets.exception.business.IngressoNaoUtilizadoException;
import com.es.cinema.tickets.exception.notfound.FilmeNotFoundException;
import com.es.cinema.tickets.exception.notfound.IngressoNotFoundException;
import com.es.cinema.tickets.persistence.entity.Avaliacao;
import com.es.cinema.tickets.persistence.entity.Ingresso;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.enums.StatusIngresso;
import com.es.cinema.tickets.persistence.repository.AvaliacaoRepository;
import com.es.cinema.tickets.persistence.repository.FilmeRepository;
import com.es.cinema.tickets.persistence.repository.IngressoRepository;
import com.es.cinema.tickets.web.dto.request.AvaliacaoRequest;
import com.es.cinema.tickets.web.dto.response.AvaliacaoRegistradaResponse;
import com.es.cinema.tickets.web.dto.response.AvaliacaoResponse;
import com.es.cinema.tickets.web.mapper.AvaliacaoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final IngressoRepository ingressoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final FilmeRepository filmeRepository;
    private final AvaliacaoMapper avaliacaoMapper;

    @Transactional
    public AvaliacaoRegistradaResponse registrar(AvaliacaoRequest request, Long usuarioId) {
        Ingresso ingresso = ingressoRepository.findByCodigoComDetalhes(request.getIngressoId())
                .orElseThrow(() -> new IngressoNotFoundException(request.getIngressoId()));

        // Garante que o ingresso pertence ao usuário autenticado
        if (!ingresso.getUsuario().getId().equals(usuarioId)) {
            throw new IngressoAcessoNegadoException();
        }

        // Ingresso deve ter sido utilizado
        if (!StatusIngresso.UTILIZADO.equals(ingresso.getStatus())) {
            if (StatusIngresso.AVALIADO.equals(ingresso.getStatus())) {
                throw new IngressoJaAvaliadoException();
            }
            throw new IngressoNaoUtilizadoException();
        }

        // Garante que a sessão já encerrou
        Sessao sessao = ingresso.getPedido().getSessao();
        if (sessao.getInicio().isAfter(LocalDateTime.now())) {
            throw new IngressoNaoUtilizadoException();
        }

        Avaliacao avaliacao = Avaliacao.builder()
                .ingresso(ingresso)
                .filme(sessao.getFilme())
                .sessao(sessao)
                .usuario(ingresso.getUsuario())
                .nota(request.getNota())
                .comentario(request.getComentario())
                .build();

        avaliacaoRepository.save(avaliacao);

        // Marca o ingresso como AVALIADO
        ingresso.avaliar();
        ingressoRepository.save(ingresso);

        log.info("Avaliação registrada. avaliacaoId={}, ingressoId={}, usuarioId={}",
                avaliacao.getId(), ingresso.getId(), usuarioId);

        return avaliacaoMapper.toRegistradaResponse(avaliacao);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> listarPorFilme(Long filmeId) {
        if (!filmeRepository.existsById(filmeId)) {
            throw new FilmeNotFoundException(filmeId);
        }
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAllByFilmeId(filmeId);
        return avaliacaoMapper.toResponseList(avaliacoes);
    }
}
