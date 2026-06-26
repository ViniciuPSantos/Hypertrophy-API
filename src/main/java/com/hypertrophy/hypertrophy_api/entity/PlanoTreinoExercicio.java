package com.hypertrophy.hypertrophy_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "plano_treino_exercicios")
public class PlanoTreinoExercicio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_treino_id", nullable = false)
    private PlanoTreino planoTreino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_id", nullable = false)
    private Exercicio exercicio;

    @Column(name = "dia_da_semana", nullable = false)
    private Integer diaDaSemana;

    @Column(name = "series_alvo", nullable = false)
    private Integer seriesAlvo;

    @Column(name = "repeticoes_alvo_min", nullable = false)
    private Integer repeticoesAlvoMin;

    @Column(name = "repeticoes_alvo_max", nullable = false)
    private Integer repeticoesAlvoMax;

    @Column(nullable = false)
    private Integer ordem;
}

