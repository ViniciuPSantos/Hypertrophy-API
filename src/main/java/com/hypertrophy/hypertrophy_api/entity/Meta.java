package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.TipoMeta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "metas")
public class Meta extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMeta tipo;

    @Column(name = "valor_alvo", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAlvo;

    @Column(name = "data_alvo")
    private LocalDate dataAlvo;

    @Column(nullable = false)
    private Boolean ativo = true;
}