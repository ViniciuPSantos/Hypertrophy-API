package com.hypertrophy.hypertrophy_api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.hypertrophy.hypertrophy_api.dto.auth.AuthResponse;
import com.hypertrophy.hypertrophy_api.dto.auth.RegisterRequest;
import com.hypertrophy.hypertrophy_api.dto.auth.LoginRequest;
import com.hypertrophy.hypertrophy_api.dto.common.ApiResponse;
import com.hypertrophy.hypertrophy_api.service.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> registrar(@Valid @RequestBody RegisterRequest request){
        return ApiResponse.ok("Usuário criado com sucesso", authService.registrar(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ApiResponse.ok(authService.login(request));
    }

}
