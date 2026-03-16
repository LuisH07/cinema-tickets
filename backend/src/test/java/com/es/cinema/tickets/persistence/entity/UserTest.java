package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserEntity() {
        User user = User.builder()
                .id(1L)
                .email("teste@example.com")
                .passwordHash("senha123")
                .role(Role.ADMIN)
                .nome("Luis")
                .cpf("12345678900")
                .celular("11999999999")
                .build();

        // Testando getters
        assertEquals(1L, user.getId());
        assertEquals("teste@example.com", user.getEmail());
        assertEquals("senha123", user.getPasswordHash());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals("Luis", user.getNome());
        assertEquals("12345678900", user.getCpf());
        assertEquals("11999999999", user.getCelular());

        // Testando métodos de atualização
        user.changePassword("novaSenha");
        assertEquals("novaSenha", user.getPasswordHash());

        user.changeRole(Role.USER);
        assertEquals(Role.USER, user.getRole());

        user.updateContact("11988888888");
        assertEquals("11988888888", user.getCelular());

        // toString
        String str = user.toString();
        assertTrue(str.contains("User"));
        assertTrue(str.contains("email='teste@example.com'"));
        assertTrue(str.contains("role=USER"));

        // equals e hashCode
        User mesmaId = User.builder().id(1L).build();
        assertEquals(user, mesmaId);
        assertEquals(user.hashCode(), mesmaId.hashCode());

        User diferenteId = User.builder().id(2L).build();
        assertNotEquals(user, diferenteId);
        assertNotEquals(user.hashCode(), diferenteId.hashCode());
    }
}