package com.es.cinema.tickets.bootstrap;

import com.es.cinema.tickets.persistence.entity.User;
import com.es.cinema.tickets.persistence.enums.Role;
import com.es.cinema.tickets.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class ProdAdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap-admin.enabled:false}")
    private boolean enabled;

    @Value("${app.bootstrap-admin.email:admin@prod.dev}")
    private String email;

    @Value("${app.bootstrap-admin.password:password}")
    private String password;

    @Value("${app.bootstrap-admin.cpf:00000000000}")
    private String cpf;

    @Value("${app.bootstrap-admin.nome:Admin}")
    private String nome;

    @Value("${app.bootstrap-admin.celular:81999990000}")
    private String celular;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        if (!enabled) return;

        if (email.isBlank() || password.isBlank()) {
            throw new IllegalStateException("Bootstrap admin enabled, but email/password not provided");
        }

        boolean hasAnyAdmin = userRepository.existsByRole(Role.ADMIN);
        if (hasAnyAdmin) return;

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Bootstrap admin email already exists: " + email);
        }

        User admin = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .nome(nome)
                .cpf(cpf)
                .celular(celular.isBlank() ? null : celular)
                .build();

        userRepository.save(admin);
    }
}
