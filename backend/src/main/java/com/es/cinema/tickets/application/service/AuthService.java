package com.es.cinema.tickets.application.service;

import com.es.cinema.tickets.exception.business.CpfAlreadyRegisteredException;
import com.es.cinema.tickets.exception.business.EmailAlreadyRegisteredException;
import com.es.cinema.tickets.exception.business.InvalidCredentialsException;
import com.es.cinema.tickets.persistence.enums.Role;
import com.es.cinema.tickets.persistence.entity.User;
import com.es.cinema.tickets.persistence.repository.UserRepository;
import com.es.cinema.tickets.security.AuthUserDetails;
import com.es.cinema.tickets.security.JwtService;
import com.es.cinema.tickets.web.dto.request.LoginRequest;
import com.es.cinema.tickets.web.dto.request.RegisterRequest;
import com.es.cinema.tickets.web.dto.response.LoginResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyRegisteredException(request.getEmail());
        }

        if (userRepository.existsByCpf(request.getCpf())) {
            throw new CpfAlreadyRegisteredException(request.getCpf());
        }

        Role role = Role.USER;

        var user = User.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .cpf(request.getCpf())
                .celular(request.getCelular())
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException authenticationException) {
            throw new InvalidCredentialsException();
        }

        Object principalObject = authentication.getPrincipal();

        if (!(principalObject instanceof AuthUserDetails principal)) {
            throw new IllegalStateException("Unexpected authentication principal type");
        }

        Role role = principal.getRole();
        String token = jwtService.generateToken(defaultClaims(role), principal);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationSeconds())
                .build();
    }

    private Map<String, Object> defaultClaims(Role role) {
        return Map.of("role", role.name());
    }
}
