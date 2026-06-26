package com.hypertrophy.hypertrophy_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contextos_treino")
public class ContextoTreino extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(name = "equipamento_disponivel", columnDefinition = "JSON")
    private String equipamentoDisponivel;

    @Column(nullable = false)
    private Boolean ativo = false;
}
