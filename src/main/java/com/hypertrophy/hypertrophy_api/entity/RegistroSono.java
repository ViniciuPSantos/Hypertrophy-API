package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.OrigemSono;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "registros_sono")
public class RegistroSono extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "horario_dormir", nullable = false)
    private LocalDateTime horarioDormir;

    @Column(name = "horario_acordar", nullable = false)
    private LocalDateTime horarioAcordar;

    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;

    // Escala 1–5
    @Column(name = "qualidade_percebida", nullable = false)
    private Integer qualidadePercebida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrigemSono origem = OrigemSono.MANUAL;
}
