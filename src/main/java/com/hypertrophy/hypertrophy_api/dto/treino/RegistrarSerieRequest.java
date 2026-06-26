package com.hypertrophy.hypertrophy_api.dto.treino;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RegistrarSerieRequest(
        @NotNull Long exercicioId,
        @NotNull @Positive Integer numeroSerie,
        @NotNull @Positive BigDecimal cargaKg,
        @NotNull @Positive Integer repeticoesRealizadas,
        BigDecimal rpe,           // opcional — RF-103
        String observacao,        // opcional — RF-103
        Long versaoAnteriorId     // preenchido apenas em edição (RN-104)
) {}
