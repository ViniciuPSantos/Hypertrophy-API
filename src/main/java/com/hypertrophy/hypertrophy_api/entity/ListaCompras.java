package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.OrigemListaCompras;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "lista_compras")
public class ListaCompras extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alimento_id", nullable = false)
    private Alimento alimento;

    @Column(name = "quantidade_sugerida", precision = 10, scale = 2)
    private BigDecimal quantidadeSugerida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrigemListaCompras origem;

    @Column(nullable = false)
    private Boolean comprado = false;
}
