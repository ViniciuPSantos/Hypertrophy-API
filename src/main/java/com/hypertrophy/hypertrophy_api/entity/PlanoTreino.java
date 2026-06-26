package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.SplitTipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "planos_treino")
public class PlanoTreino extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contexto_treino_id", nullable = false)
    private ContextoTreino contextoTreino;

    @Column(nullable = false, length = 255)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "split_tipo", nullable = false, length = 50)
    private SplitTipo splitTipo;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "gerado_por_ia", nullable = false)
    private Boolean geradoPorIa = true;

    @Column(nullable = false)
    private Integer versao = 1;
}
