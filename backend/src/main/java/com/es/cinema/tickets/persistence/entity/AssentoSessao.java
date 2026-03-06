package com.es.cinema.tickets.persistence.entity;

import com.es.cinema.tickets.persistence.enums.StatusAssento;
import com.es.cinema.tickets.persistence.enums.TipoAssento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "assentos_sessao",
        uniqueConstraints = @UniqueConstraint(
            name = "uk_assento_sessao_codigo",
            columnNames = {"sessao_id", "codigo"}
        )
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssentoSessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @NotBlank
    @Column(nullable = false, length = 10)
    private String codigo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoAssento tipo;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAssento status;

    @Column(length = 50, unique = true)
    private String ingressoId;

    public boolean isDisponivel() {
        return StatusAssento.DISPONIVEL.equals(this.status);
    }

    public void ocupar() {
        this.status = StatusAssento.OCUPADO;
    }

    public void vender(String ingressoId) {
        this.status = StatusAssento.VENDIDO;
        this.ingressoId = ingressoId;
    }

    public void liberar() {
        this.status = StatusAssento.DISPONIVEL;
        this.ingressoId = null;
    }

    @Override
    public String toString() {
        return "AssentoSessao{id=" + id + ", codigo='" + codigo + "', tipo=" + tipo + ", status=" + status + "}";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssentoSessao other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}
