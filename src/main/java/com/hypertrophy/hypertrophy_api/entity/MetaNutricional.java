package com.hypertrophy.hypertrophy_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "metas_nutricionais")
public class MetaNutricional extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "data_vigencia_inicio", nullable = false)
    private LocalDate dataVigenciaInicio;

    @Column(name = "calorias_alvo", nullable = false, precision = 8, scale = 2)
    private BigDecimal caloriasAlvo;

    @Column(name = "proteina_alvo_g", nullable = false, precision = 7, scale = 2)
    private BigDecimal proteinaAlvoG;

    @Column(name = "carboidrato_alvo_g", nullable = false, precision = 7, scale = 2)
    private BigDecimal carboidratoAlvoG;

    @Column(name = "gordura_alvo_g", nullable = false, precision = 7, scale = 2)
    private BigDecimal gorduraAlvoG;

    @Column(name = "motivo_recalculo", columnDefinition = "TEXT")
    private String motivoRecalculo;
}

