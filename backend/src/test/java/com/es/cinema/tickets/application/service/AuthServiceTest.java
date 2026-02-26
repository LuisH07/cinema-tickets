package com.es.cinema.tickets.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.es.cinema.tickets.persistence.entity.User;
import com.es.cinema.tickets.persistence.repository.UserRepository;
import com.es.cinema.tickets.web.dto.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_ShouldSaveUser_WhenDataIsValid() {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Hugo");
        request.setEmail("hugo@teste.com");
        request.setCpf("12345678900");
        request.setPassword("senha123");
        request.setCelular("11999999999");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByCpf(request.getCpf())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("senhaEncriptada");

        authService.register(request);

        verify(userRepository, times(1)).save(any(User.class));
    }
}