package com.hypertrophy.hypertrophy_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "registros_peso_medidas")
public class RegistroPesoMedidas extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "peso_kg", precision = 5, scale = 2)
    private BigDecimal pesoKg;

    @Column(name = "percentual_gordura", precision = 5, scale = 2)
    private BigDecimal percentualGordura;

    // Objeto JSON flexível: { "braco_cm": 38.5, "cintura_cm": 80, "coxa_cm": 58 }
    @Column(columnDefinition = "JSON")
    private String medidas;
}