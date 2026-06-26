package com.hypertrophy.hypertrophy_api.dto.auth;

public record AuthResponse(
        String token,
        String email,
        boolean onboardingCompleto
) {}
