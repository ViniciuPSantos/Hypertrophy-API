package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.TipoHabito;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "habitos")
public class Habito extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoHabito tipo;

    @Column(nullable = false)
    private Boolean ativo = true;
}
