package com.hypertrophy.hypertrophy_api.dto.treino;

import java.util.List;

public record FinalizarSessaoResponse(
        Long sessaoId,
        int duracaoSegundos,
        int totalSeries,
        List<String> recordesPessoais,   // RF-105: PRs detectados
        String resumoTexto
) {}

