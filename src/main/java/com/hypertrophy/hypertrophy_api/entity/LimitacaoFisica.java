package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.RegiaoLimitacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "limitacoes_fisicas")
public class LimitacaoFisica extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RegiaoLimitacao regiao;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;
}