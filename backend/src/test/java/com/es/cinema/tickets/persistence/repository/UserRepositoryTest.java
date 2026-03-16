package com.es.cinema.tickets.persistence.repository;

import com.es.cinema.tickets.persistence.entity.User;
import com.es.cinema.tickets.persistence.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        entityManager.persist(User.builder()
                .email("admin@example.com")
                .passwordHash("hash123")
                .role(Role.ADMIN)
                .nome("Admin User")
                .cpf("12345678901")
                .build());

        entityManager.persist(User.builder()
                .email("user@example.com")
                .passwordHash("hash456")
                .role(Role.USER)
                .nome("Normal User")
                .cpf("98765432100")
                .build());

        entityManager.flush();
    }

    @Test
    void deveEncontrarUsuarioPorEmail() {
        Optional<User> encontrado = repository.findByEmail("admin@example.com");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Admin User");
    }

    @Test
    void deveVerificarExistenciaPorEmail() {
        assertThat(repository.existsByEmail("admin@example.com")).isTrue();
        assertThat(repository.existsByEmail("naoexiste@example.com")).isFalse();
    }

    @Test
    void deveVerificarExistenciaPorRole() {
        assertThat(repository.existsByRole(Role.ADMIN)).isTrue();
        assertThat(repository.existsByRole(Role.USER)).isTrue();
    }

    @Test
    void deveVerificarExistenciaPorCpf() {
        assertThat(repository.existsByCpf("12345678901")).isTrue();
        assertThat(repository.existsByCpf("00000000000")).isFalse();
    }
}