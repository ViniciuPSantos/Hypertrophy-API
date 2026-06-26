package com.hypertrophy.hypertrophy_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "refeicao_itens")
public class RefeicaoItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refeicao_id", nullable = false)
    private RefeicaoRegistrada refeicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alimento_id", nullable = false)
    private Alimento alimento;

    @Column(name = "quantidade_g", nullable = false, precision = 8, scale = 2)
    private BigDecimal quantidadeG;
}
