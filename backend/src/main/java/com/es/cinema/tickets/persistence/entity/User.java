package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column 
    private String celular;

    public void changePassword(String newEncodedPassword) {
        this.passwordHash = newEncodedPassword;
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
    }

    public void updateContact(String celular) {
        this.celular = celular;
    }

    @Override
    public String toString() {
        return "User{id=" + id +
                ", email='" + email +
                "', role=" + role +
                "}";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}