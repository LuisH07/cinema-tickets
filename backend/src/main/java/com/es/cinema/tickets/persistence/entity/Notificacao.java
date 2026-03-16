package com.es.cinema.tickets.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Getter
@Setter 
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "device_token", nullable = false)
    private String deviceToken;

    @NotBlank
    @Column(nullable = false)
    private String mensagem;

    @NotNull
    @Column(name = "data_envio_agendada", nullable = false)
    private LocalDateTime dataEnvioAgendada;

    @NotNull
    @Column(nullable = false)
    private boolean enviado = false;

    @NotNull
    @Column(name = "sessao_id", nullable = false)
    private Long sessaoId;

    @Column(name = "titulo_filme")
    private String tituloFilme;

    @Column(length = 100)
    private String sala;

    @Column(length = 5)
    private String horario;

    @Column(nullable = false)
    @Builder.Default
    private boolean visto = false;

    @Override
    public String toString() {
        return "Notificacao{id=" + id + ", dataEnvio=" + dataEnvioAgendada + ", enviado=" + enviado + "}";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notificacao other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }
}