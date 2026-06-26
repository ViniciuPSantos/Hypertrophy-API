package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.OrigemRefeicao;
import com.hypertrophy.hypertrophy_api.entity.enums.TipoRefeicao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "refeicoes_registradas")
public class RefeicaoRegistrada extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRefeicao tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrigemRefeicao origem;

    @Column(name = "total_calorias", precision = 8, scale = 2)
    private BigDecimal totalCalorias;

    @Column(name = "total_proteina", precision = 7, scale = 2)
    private BigDecimal totalProteina;

    @Column(name = "total_carboidrato", precision = 7, scale = 2)
    private BigDecimal totalCarboidrato;

    @Column(name = "total_gordura", precision = 7, scale = 2)
    private BigDecimal totalGordura;
}
