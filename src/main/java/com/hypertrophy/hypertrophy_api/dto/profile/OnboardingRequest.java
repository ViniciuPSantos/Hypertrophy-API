package com.hypertrophy.hypertrophy_api.dto.profile;

import com.hypertrophy.hypertrophy_api.entity.enums.NivelExperiencia;
import com.hypertrophy.hypertrophy_api.entity.enums.RegiaoLimitacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OnboardingRequest(
        @NotBlank String nome,
        LocalDate dataNascimento,
        BigDecimal alturaCm,
        BigDecimal pesoKgInicial,
        BigDecimal percentualGorduraInicial,
        @NotNull NivelExperiencia nivelExperiencia,
        @NotNull Integer frequenciaSemanal,
        // RF-003: contexto de treino inicial
        @NotBlank String nomeContexto,
        @NotNull List<String> equipamentosDisponiveis,
        // RF-001: limitações físicas
        List<LimitacaoRequest> limitacoes
) {
    public record LimitacaoRequest(
            @NotNull RegiaoLimitacao regiao,
            String descricao
    ) {}
}
