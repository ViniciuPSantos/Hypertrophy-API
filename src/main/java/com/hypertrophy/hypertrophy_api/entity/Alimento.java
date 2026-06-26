package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.CategoriaAlimento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "alimentos")
public class Alimento extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CategoriaAlimento categoria;

    @Column(name = "calorias_por_100g", nullable = false, precision = 7, scale = 2)
    private BigDecimal caloriasPor100g;

    @Column(name = "proteina_por_100g", nullable = false, precision = 6, scale = 2)
    private BigDecimal proteinaPor100g;

    @Column(name = "carboidrato_por_100g", nullable = false, precision = 6, scale = 2)
    private BigDecimal carboidratoPor100g;

    @Column(name = "gordura_por_100g", nullable = false, precision = 6, scale = 2)
    private BigDecimal gorduraPor100g;

    @Column(name = "fibra_por_100g", precision = 6, scale = 2)
    private BigDecimal fibraPor100g;
}
