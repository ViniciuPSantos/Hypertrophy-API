package com.hypertrophy.hypertrophy_api.dto.treino;

import com.hypertrophy.hypertrophy_api.entity.enums.StatusSessao;
import java.time.LocalDateTime;

public record SessaoResponse(
        Long id,
        Long planoTreinoId,
        LocalDateTime dataInicio,
        StatusSessao status
) {}
