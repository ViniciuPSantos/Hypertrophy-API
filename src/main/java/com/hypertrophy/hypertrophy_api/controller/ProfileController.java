package com.hypertrophy.hypertrophy_api.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.hypertrophy.hypertrophy_api.dto.common.*;
import com.hypertrophy.hypertrophy_api.dto.profile.*;
import com.hypertrophy.hypertrophy_api.entity.User;
import com.hypertrophy.hypertrophy_api.service.ProfileService;

@RestController
@RequestMapping("/api/v1/perfil")
public class ProfileController {
    
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @PostMapping("/onboarding")
    public ApiResponse<ProfileResponse> completarOnboarding(@AuthenticationPrincipal User user, @Valid @RequestBody OnboardingRequest request){
        return ApiResponse.ok(profileService.completarOnboarding(user.getId(), request));
    }

    @GetMapping
    public ApiResponse<ProfileResponse> buscarPerfil(@AuthenticationPrincipal User user){
        return ApiResponse.ok(profileService.buscarPorUserId(user.getId()));
    }
}
