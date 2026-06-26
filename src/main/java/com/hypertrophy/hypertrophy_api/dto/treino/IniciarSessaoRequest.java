package com.hypertrophy.hypertrophy_api.dto.treino;

public record IniciarSessaoRequest(
        Long planoTreinoId  // nullable — permite treino livre (RF-102)
) {}
