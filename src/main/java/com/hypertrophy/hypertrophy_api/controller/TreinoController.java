package com.hypertrophy.hypertrophy_api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.hypertrophy.hypertrophy_api.dto.common.ApiResponse;
import com.hypertrophy.hypertrophy_api.dto.treino.FinalizarSessaoResponse;
import com.hypertrophy.hypertrophy_api.dto.treino.IniciarSessaoRequest;
import com.hypertrophy.hypertrophy_api.dto.treino.RegistrarSerieRequest;
import com.hypertrophy.hypertrophy_api.dto.treino.SessaoResponse;
import com.hypertrophy.hypertrophy_api.entity.User;
import com.hypertrophy.hypertrophy_api.service.TreinoService;

@RestController
@RequestMapping("/api/v1/treino")
public class TreinoController{

    private final TreinoService treinoService;

    public TreinoController(TreinoService treinoService){
        this.treinoService = treinoService;
    }

    @PostMapping("/sessoes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SessaoResponse> iniciarSessao(@AuthenticationPrincipal User user, @RequestBody IniciarSessaoRequest request){
        return ApiResponse.ok(treinoService.iniciarSessao(user.getId(), request));
    }

    @PostMapping("/sessoes/{sessaoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> registrarSerie(@AuthenticationPrincipal User user, @PathVariable Long sessaoId, @Valid @RequestBody RegistrarSerieRequest request){
        treinoService.registrarSerie(user.getId(), sessaoId, request);
        return ApiResponse.ok("Série registrada.", null);
    }

    @PostMapping("/sessoes/{sessaoId}/finalizar")
    public ApiResponse<FinalizarSessaoResponse> finalizarSessao(@AuthenticationPrincipal User user, @PathVariable Long sessaoId){
        return ApiResponse.ok(treinoService.finalizarSessao(user.getId(), sessaoId));
    }

    @PostMapping("/sessoes/{sessaoId}/abandonar")
    public ApiResponse<Void> abandonarSessao(@AuthenticationPrincipal User user, @PathVariable Long sessaoId){
        treinoService.abandonarSessao(user.getId(), sessaoId);
        return ApiResponse.ok("Sessão abandonada", null);
    }
}
