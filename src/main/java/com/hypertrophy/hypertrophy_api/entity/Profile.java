package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.NivelExperiencia;
import com.hypertrophy.hypertrophy_api.entity.enums.Objetivo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "altura_cm", precision = 5, scale = 2)
    private BigDecimal alturaCm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Objetivo objetivo = Objetivo.HIPERTROFIA;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_experiencia", length = 50)
    private NivelExperiencia nivelExperiencia;

    @Column(name = "onboarding_completo", nullable = false)
    private Boolean onboardingCompleto = false;
}