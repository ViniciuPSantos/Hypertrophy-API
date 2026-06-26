package com.hypertrophy.hypertrophy_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "habito_registros")
public class HabitoRegistro extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habito_id", nullable = false)
    private Habito habito;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private Boolean concluido = false;
}