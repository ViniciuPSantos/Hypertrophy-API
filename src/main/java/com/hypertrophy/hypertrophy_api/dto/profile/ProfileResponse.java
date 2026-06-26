package com.hypertrophy.hypertrophy_api.dto.profile;

import com.hypertrophy.hypertrophy_api.entity.enums.NivelExperiencia;
import com.hypertrophy.hypertrophy_api.entity.enums.Objetivo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProfileResponse(
        Long userId,
        String nome,
        LocalDate dataNascimento,
        BigDecimal alturaCm,
        Objetivo objetivo,
        NivelExperiencia nivelExperiencia,
        boolean onboardingCompleto
) {}