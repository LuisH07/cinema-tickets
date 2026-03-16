package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.notfound.SessaoNotFoundException;
import com.es.cinema.tickets.persistence.entity.AssentoSessao;
import com.es.cinema.tickets.persistence.entity.Filme;
import com.es.cinema.tickets.persistence.entity.Sala;
import com.es.cinema.tickets.persistence.entity.Sessao;
import com.es.cinema.tickets.persistence.enums.StatusAssento;
import com.es.cinema.tickets.persistence.enums.TipoAssento;
import com.es.cinema.tickets.persistence.repository.AssentoSessaoRepository;
import com.es.cinema.tickets.persistence.repository.SessaoRepository;
import com.es.cinema.tickets.web.dto.request.SessaoRequest;
import com.es.cinema.tickets.web.dto.response.SessaoResponse;
import com.es.cinema.tickets.web.mapper.SessaoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private SessaoMapper sessaoMapper;

    @Mock
    private AssentoSessaoRepository assentoSessaoRepository;

    @Mock
    private FilmeService filmeService;

    @Mock
    private SalaService salaService;

    @InjectMocks
    private SessaoService sessaoService;

    @Captor
    private ArgumentCaptor<List<AssentoSessao>> assentosCaptor;

    private Filme filmeMock;
    private Sala salaMock;
    private Sessao sessaoMock;

    @BeforeEach
    void setUp() {
        filmeMock = Filme.builder().id(1L).titulo("O Auto da Compadecida 2").build();
        
        salaMock = Sala.builder().id(1L).nome("Sala IMAX").capacidade(30).build();
        
        sessaoMock = Sessao.builder()
                .id(1L)
                .filme(filmeMock)
                .sala(salaMock)
                .inicio(LocalDateTime.now())
                .build();
    }

    @Test
    void criar_shouldCreateSessaoAndGenerateAssentosCorrectly() {
        SessaoRequest requestMock = mock(SessaoRequest.class);
        when(requestMock.getFilmeId()).thenReturn(1L);
        when(requestMock.getSalaId()).thenReturn(1L);

        SessaoResponse responseMock = mock(SessaoResponse.class);

        when(filmeService.getOrThrow(1L)).thenReturn(filmeMock);
        when(salaService.getOrThrow(1L)).thenReturn(salaMock);
        when(sessaoMapper.toEntity(requestMock, filmeMock, salaMock)).thenReturn(sessaoMock);
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessaoMock);
        when(sessaoMapper.toResponse(sessaoMock)).thenReturn(responseMock);

        SessaoResponse result = sessaoService.criar(requestMock);

        assertNotNull(result);
        verify(sessaoRepository).save(sessaoMock);
        
        verify(assentoSessaoRepository).saveAll(assentosCaptor.capture());
        List<AssentoSessao> assentosGerados = assentosCaptor.getValue();

        assertEquals(30, assentosGerados.size(), "Deve gerar exatamente 30 assentos");

        AssentoSessao assentoA1 = assentosGerados.stream().filter(a -> a.getCodigo().equals("A1")).findFirst().get();
        assertEquals(TipoAssento.VIP, assentoA1.getTipo());
        assertEquals(new BigDecimal("40.00"), assentoA1.getValor());
        assertEquals(StatusAssento.DISPONIVEL, assentoA1.getStatus());

        AssentoSessao assentoB5 = assentosGerados.stream().filter(a -> a.getCodigo().equals("B5")).findFirst().get();
        assertEquals(TipoAssento.VIP, assentoB5.getTipo());

        AssentoSessao assentoC1 = assentosGerados.stream().filter(a -> a.getCodigo().equals("C1")).findFirst().get();
        assertEquals(TipoAssento.COMUM, assentoC1.getTipo());
        assertEquals(new BigDecimal("25.00"), assentoC1.getValor());

        AssentoSessao assentoC9 = assentosGerados.stream().filter(a -> a.getCodigo().equals("C9")).findFirst().get();
        assertEquals(TipoAssento.PNE, assentoC9.getTipo());
        assertEquals(new BigDecimal("25.00"), assentoC9.getValor());
    }

    @Test
    void listarPorData_shouldReturnSessoesInRange() {
        LocalDate dataBusca = LocalDate.of(2026, 3, 16);
        LocalDateTime inicioDia = dataBusca.atStartOfDay();
        LocalDateTime inicioProximoDia = dataBusca.plusDays(1).atStartOfDay();

        List<Sessao> sessoes = List.of(sessaoMock);
        SessaoResponse responseMock = mock(SessaoResponse.class);

        when(sessaoRepository.findByInicioBetween(inicioDia, inicioProximoDia)).thenReturn(sessoes);
        when(sessaoMapper.toResponseList(sessoes)).thenReturn(List.of(responseMock));

        List<SessaoResponse> result = sessaoService.listarPorData(dataBusca);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(sessaoRepository).findByInicioBetween(inicioDia, inicioProximoDia);
    }

    @Test
    void buscarPorId_shouldReturnSessao_whenExists() {
        Long id = 1L;
        SessaoResponse responseMock = mock(SessaoResponse.class);

        when(sessaoRepository.findWithFilmeAndSalaById(id)).thenReturn(Optional.of(sessaoMock));
        when(sessaoMapper.toResponse(sessaoMock)).thenReturn(responseMock);

        SessaoResponse result = sessaoService.buscarPorId(id);

        assertNotNull(result);
        verify(sessaoRepository).findWithFilmeAndSalaById(id);
    }

    @Test
    void buscarPorId_shouldThrowException_whenNotFound() {
        Long id = 99L;
        when(sessaoRepository.findWithFilmeAndSalaById(id)).thenReturn(Optional.empty());

        SessaoNotFoundException exception = assertThrows(SessaoNotFoundException.class, () -> 
            sessaoService.buscarPorId(id)
        );

        assertTrue(exception.getMessage().contains("99") || exception.getClass().getSimpleName().equals("SessaoNotFoundException"));
        verify(sessaoRepository).findWithFilmeAndSalaById(id);
        verifyNoInteractions(sessaoMapper);
    }
}