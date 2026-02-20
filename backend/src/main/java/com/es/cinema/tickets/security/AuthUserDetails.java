package com.es.cinema.tickets.security;

import com.es.cinema.tickets.persistence.enums.Role;
import com.es.cinema.tickets.persistence.entity.User;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class AuthUserDetails implements UserDetails {
    @Getter
    private final Long id;

    @Getter
    private final Role role;

    private final String email;
    private final String password;

    public AuthUserDetails(User user) {
        Objects.requireNonNull(user, "User cannot be null");

        this.id = user.getId();
        this.email = Objects.requireNonNull(user.getEmail());
        this.password = Objects.requireNonNull(user.getPasswordHash());
        this.role = Objects.requireNonNull(user.getRole());
    }

    @NonNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @NonNull
    @Override
    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    public String getUsername() {
        return email;
    }

}


