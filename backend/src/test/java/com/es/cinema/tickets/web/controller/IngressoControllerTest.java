package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.application.service.IngressoService;
import com.es.cinema.tickets.security.AuthUserDetails;
import com.es.cinema.tickets.web.dto.response.IngressoDetalheResponse;
import com.es.cinema.tickets.web.dto.response.IngressoResumoResponse;
import com.es.cinema.tickets.web.dto.response.IngressosListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
class IngressoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngressoService ingressoService;

    @Test
    void buscarPorCodigo_shouldReturnDetalheIngresso() throws Exception {
        String codigo = "ABC123";

        // Criando mock de AuthUserDetails com ID
        AuthUserDetails authUser = mock(AuthUserDetails.class);
        when(authUser.getId()).thenReturn(1L);

        var response = IngressoDetalheResponse.builder()
                .ingressoId(codigo)
                .status("CONFIRMADO")
                .cliente(IngressoDetalheResponse.ClienteResponse.builder()
                        .nome("Luis")
                        .cpf("12345678900")
                        .build())
                .build();

        // Usar ID do mock
        when(ingressoService.buscarPorCodigo(codigo, 1L)).thenReturn(response);

        mockMvc.perform(get("/ingressos/{codigo}", codigo)
                        .with(user(authUser))) // passa o AuthUserDetails
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingresso_id").value(codigo))
                .andExpect(jsonPath("$.status").value("CONFIRMADO"))
                .andExpect(jsonPath("$.cliente.nome").value("Luis"));
    }

    @Test
    void listar_shouldReturnListaIngressos() throws Exception {
        AuthUserDetails authUser = mock(AuthUserDetails.class);
        when(authUser.getId()).thenReturn(1L);

        var response = IngressosListResponse.builder()
                .ingressos(List.of(
                        IngressoResumoResponse.builder().id("1").filme("Filme X").build()
                ))
                .build();

        when(ingressoService.listarPorUsuario(1L)).thenReturn(response);

        mockMvc.perform(get("/ingressos")
                        .with(user(authUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingressos[0].filme").value("Filme X"));
    }
}