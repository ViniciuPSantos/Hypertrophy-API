package com.hypertrophy.hypertrophy_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "series_executadas")
public class SerieExecutada extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_treino_id", nullable = false)
    private SessaoTreino sessaoTreino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_id", nullable = false)
    private Exercicio exercicio;

    @Column(name = "numero_serie", nullable = false)
    private Integer numeroSerie;

    @Column(name = "carga_kg", nullable = false, precision = 6, scale = 2)
    private BigDecimal cargaKg;

    @Column(name = "repeticoes_realizadas", nullable = false)
    private Integer repeticoesRealizadas;

    // Escala 1–10 com décimos (ex: 7.5) — RF-103
    @Column(precision = 3, scale = 1)
    private BigDecimal rpe;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    // RN-104: nunca apaga histórico — edições criam novo registro referenciando o anterior
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "versao_anterior_id")
    private SerieExecutada versaoAnterior;
}