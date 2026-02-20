package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.StatusFilme;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "filmes")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titulo;

    @NotBlank
    @Column(nullable = false)
    private String poster;

    @NotBlank
    @Column(nullable = false)
    private String backdrop;

    @NotBlank
    @Column(nullable = false)
    private String classificacao;

    @Column(nullable = false)
    private int duracao;

    @ElementCollection
    @CollectionTable(
            name = "filme_generos",
            joinColumns = @JoinColumn(name = "filme_id", nullable = false)
    )
    @Column(name = "genero", nullable = false)
    @Builder.Default
    private List<String> generos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "filme_diretores",
            joinColumns = @JoinColumn(name = "filme_id", nullable = false)
    )
    @Column(name = "diretor", nullable = false)
    @Builder.Default
    private List<String> diretores = new ArrayList<>();

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String sinopse;

    @ElementCollection
    @CollectionTable(
            name = "filme_elenco",
            joinColumns = @JoinColumn(name = "filme_id", nullable = false)
    )
    @Column(name = "ator", nullable = false)
    @Builder.Default
    private List<String> elenco = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFilme status;

    @Override
    public String toString() {
        return "Filme{id=" + id + ", titulo='" + titulo + "', status=" + status + "}";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Filme other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}
