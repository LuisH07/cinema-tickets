package com.es.cinema.tickets.bootstrap;

import com.es.cinema.tickets.persistence.entity.User;
import com.es.cinema.tickets.persistence.enums.Role;
import com.es.cinema.tickets.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(10)
@Profile("dev")
@RequiredArgsConstructor
class UserSeeder implements SeederInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void seed() {
        List<SeedUser> users = List.of(
                new SeedUser("admin@local.dev", "Admin Dev", "99999999999", "81999990000", "admin1", Role.ADMIN),

                new SeedUser("user1@local.dev", "User One", "11111111111", "81999990001", "password1", Role.USER),
                new SeedUser("user2@local.dev", "User Two", "22222222222", "81999990002", "password2", Role.USER),
                new SeedUser("user3@local.dev", "User Three", "33333333333", "81999990003", "password3", Role.USER)
        );

        for (SeedUser userSeed : users) {
            if (userRepository.existsByEmail(userSeed.email()) || userRepository.existsByCpf(userSeed.cpf())) {
                continue;
            }

            User user = User.builder()
                    .email(userSeed.email())
                    .nome(userSeed.nome())
                    .cpf(userSeed.cpf())
                    .celular(userSeed.celular())
                    .role(userSeed.role())
                    .passwordHash(passwordEncoder.encode(userSeed.rawPassword()))
                    .build();

            userRepository.save(user);
        }
    }

    private record SeedUser(
            String email,
            String nome,
            String cpf,
            String celular,
            String rawPassword,
            Role role
    ) {}
}
