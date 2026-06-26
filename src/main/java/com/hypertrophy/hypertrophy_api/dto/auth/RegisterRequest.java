package com.hypertrophy.hypertrophy_api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres") String senha,
        @NotBlank String nome
) {}
