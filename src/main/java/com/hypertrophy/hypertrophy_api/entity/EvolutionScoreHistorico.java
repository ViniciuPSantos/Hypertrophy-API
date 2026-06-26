package com.hypertrophy.hypertrophy_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "evolution_score_historico")
public class EvolutionScoreHistorico extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "score_total", nullable = false, precision = 5, scale = 2)
    private BigDecimal scoreTotal;

    @Column(name = "componente_treino", nullable = false, precision = 5, scale = 2)
    private BigDecimal componenteTreino;

    @Column(name = "componente_proteina", nullable = false, precision = 5, scale = 2)
    private BigDecimal componenteProteina;

    @Column(name = "componente_sono", nullable = false, precision = 5, scale = 2)
    private BigDecimal componenteSono;

    @Column(name = "componente_hidratacao", nullable = false, precision = 5, scale = 2)
    private BigDecimal componenteHidratacao;

    @Column(name = "componente_recuperacao", nullable = false, precision = 5, scale = 2)
    private BigDecimal componenteRecuperacao;

    @Column(name = "componente_consistencia", nullable = false, precision = 5, scale = 2)
    private BigDecimal componenteConsistencia;

    @Column(name = "componente_evolucao_corporal", nullable = false, precision = 5, scale = 2)
    private BigDecimal componenteEvolucaoCorporal;

    @Column(name = "componente_mais_fraco", length = 100)
    private String componenteMaisFraco;
}

