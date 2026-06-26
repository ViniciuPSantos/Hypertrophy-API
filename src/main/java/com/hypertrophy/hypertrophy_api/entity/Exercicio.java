package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.Equipamento;
import com.hypertrophy.hypertrophy_api.entity.enums.GrupoMuscular;
import com.hypertrophy.hypertrophy_api.entity.enums.PadraoMovimento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exercicios")
public class Exercicio extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_muscular_primario", nullable = false, length = 50)
    private GrupoMuscular grupoMuscularPrimario;

    @Column(name = "grupos_musculares_secundarios", columnDefinition = "JSON")
    private String gruposMusculatesSecundarios;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipamento_necessario", nullable = false, length = 50)
    private Equipamento equipamentoNecessario;

    @Enumerated(EnumType.STRING)
    @Column(name = "padrao_movimento", nullable = false, length = 50)
    private PadraoMovimento padraoMovimento;

    // JSON array com valores de RegiaoLimitacao — usado em RN-002
    @Column(columnDefinition = "JSON")
    private String contraindicacoes;
}
