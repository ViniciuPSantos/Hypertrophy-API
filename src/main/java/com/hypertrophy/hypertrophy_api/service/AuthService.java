package com.hypertrophy.hypertrophy_api.service;

import com.hypertrophy.hypertrophy_api.dto.auth.AuthResponse;
import com.hypertrophy.hypertrophy_api.dto.auth.LoginRequest;
import com.hypertrophy.hypertrophy_api.dto.auth.RegisterRequest;
import com.hypertrophy.hypertrophy_api.entity.Profile;
import com.hypertrophy.hypertrophy_api.entity.User;
import com.hypertrophy.hypertrophy_api.repository.ProfileRepository;
import com.hypertrophy.hypertrophy_api.repository.UserRepository;
import com.hypertrophy.hypertrophy_api.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setSenhaHash(passwordEncoder.encode(request.senha()));
        userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setNome(request.nome());
        profile.setOnboardingCompleto(false);
        profileRepository.save(profile);

        String token = tokenProvider.gerarToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), false);
    }

    public AuthResponse login(LoginRequest request){
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        String token = tokenProvider.gerarToken(auth);
        boolean onboardingCompleto = profileRepository.findByUserId(
                ((User) auth.getPrincipal()).getId()
        ).map(Profile::getOnboardingCompleto).orElse(false);

        return new AuthResponse(token, request.email(), onboardingCompleto);
    }
}
