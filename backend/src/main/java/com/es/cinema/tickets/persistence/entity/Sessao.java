package com.es.cinema.tickets.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessoes")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate data;

    @NotNull
    @Column(nullable = false)
    private LocalTime horario;

    @NotBlank
    @Column(nullable = false)
    private String classificacao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filme_id", nullable = false)
    private Filme filme;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @Builder.Default
    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssentoSessao> assentos = new ArrayList<>();

    @Override
    public String toString() {
        return "Sessao{id=" + id + ", data=" + data + ", horario=" + horario + "}";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sessao other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}
