package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.Role;
import com.es.cinema.tickets.security.AuthUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AuthUserDetailsTest {

    @Test
    void testAuthUserDetails() {
        User user = User.builder()
                .id(1L)
                .email("admin@example.com")
                .passwordHash("senha123")
                .role(Role.ADMIN)
                .nome("Admin")
                .cpf("12345678900")
                .build();

        AuthUserDetails authUser = new AuthUserDetails(user);

        // Testando getters
        assertEquals(1L, authUser.getId());
        assertEquals(Role.ADMIN, authUser.getRole());
        assertEquals("admin@example.com", authUser.getUsername());
        assertEquals("senha123", authUser.getPassword());

        // Testando authorities
        Collection<?> authorities = authUser.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));

        // Testando equals/hashCode indireto via role/email/id
        assertNotNull(authUser.toString());
    }
}