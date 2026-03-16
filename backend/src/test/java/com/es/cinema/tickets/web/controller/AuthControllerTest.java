package com.es.cinema.tickets.web.controller;

import com.es.cinema.tickets.web.dto.request.LoginRequest;
import com.es.cinema.tickets.web.dto.request.RegisterRequest;
import com.es.cinema.tickets.web.dto.response.LoginResponse;
import com.es.cinema.tickets.application.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void register_shouldCallServiceAndReturnCreated() {
        RegisterRequest request = new RegisterRequest(
                "user@example.com",
                "123456",
                "Luis",
                "12345678901",
                "11999999999"
        );

        ResponseEntity<Void> response = authController.register(request);

        // Verifica se o service foi chamado corretamente
        verify(authService).register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void login_shouldCallServiceAndReturnToken() {
        LoginRequest request = new LoginRequest("user@example.com", "123456");

        LoginResponse tokenResponse = LoginResponse.builder()
                .accessToken("fake-token")
                .tokenType("Bearer")
                .expiresIn(3600)
                .build();

        when(authService.login(request)).thenReturn(tokenResponse);

        ResponseEntity<LoginResponse> response = authController.login(request);

        verify(authService).login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("fake-token", response.getBody().getAccessToken());
        assertEquals("Bearer", response.getBody().getTokenType());
        assertEquals(3600, response.getBody().getExpiresIn());
    }
}