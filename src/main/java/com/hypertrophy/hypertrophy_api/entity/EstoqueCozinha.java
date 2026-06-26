package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.UnidadeEstoque;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "estoque_cozinha")
public class EstoqueCozinha extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alimento_id", nullable = false)
    private Alimento alimento;

    @Column(name = "quantidade_disponivel", precision = 10, scale = 2)
    private BigDecimal quantidadeDisponivel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UnidadeEstoque unidade;

    @Column(name = "consumo_medio_diario", precision = 10, scale = 2)
    private BigDecimal consumoMedioDiario;
}
