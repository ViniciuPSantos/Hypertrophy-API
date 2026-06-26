package com.hypertrophy.hypertrophy_api.service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.hypertrophy.hypertrophy_api.dto.profile.OnboardingRequest;
import com.hypertrophy.hypertrophy_api.dto.profile.ProfileResponse;
import com.hypertrophy.hypertrophy_api.entity.*;
import com.hypertrophy.hypertrophy_api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ContextoTreinoRepository contextoTreinoRepository;
    private final LimitacaoFisicaRepository limitacaoFisicaRepository;
    private final RegistroPesoMedidasRepository registroPesoMedidasRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public ProfileService(ProfileRepository profileRepository,
                          ContextoTreinoRepository contextoTreinoRepository,
                          LimitacaoFisicaRepository limitacaoFisicaRepository,
                          RegistroPesoMedidasRepository registroPesoMedidasRepository,
                          UserRepository userRepository,
                          ObjectMapper objectMapper) {
        this.profileRepository = profileRepository;
        this.contextoTreinoRepository = contextoTreinoRepository;
        this.limitacaoFisicaRepository = limitacaoFisicaRepository;
        this.registroPesoMedidasRepository = registroPesoMedidasRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ProfileResponse completarOnboarding(Long userId, OnboardingRequest request) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Profile não encontrado"));

        profile.setNome(request.nome());
        profile.setDataNascimento(request.dataNascimento());
        profile.setAlturaCm(request.alturaCm());
        profile.setNivelExperiencia(request.nivelExperiencia());
        profile.setOnboardingCompleto(true);
        profileRepository.save(profile);

        // RF-004: registro de peso/medidas inicial
        if (request.pesoKgInicial() != null) {
            User user = userRepository.getReferenceById(userId);
            RegistroPesoMedidas registro = new RegistroPesoMedidas();
            registro.setUser(user);
            registro.setDataHora(java.time.LocalDateTime.now());
            registro.setPesoKg(request.pesoKgInicial());
            registro.setPercentualGordura(request.percentualGorduraInicial());
            registroPesoMedidasRepository.save(registro);
        }

        // RF-003: contexto de treino inicial
        User user = userRepository.getReferenceById(userId);
        ContextoTreino contexto = new ContextoTreino();
        contexto.setUser(user);
        contexto.setNome(request.nomeContexto());
        contexto.setAtivo(true);
        try {
            contexto.setEquipamentoDisponivel(objectMapper.writeValueAsString(request.equipamentosDisponiveis()));
        } catch (JacksonException e) {
            contexto.setEquipamentoDisponivel("[]");
        }
        contextoTreinoRepository.save(contexto);

        // RF-001: limitações físicas — RN-002 usa esses dados para filtrar exercícios
        if (request.limitacoes() != null) {
            for (OnboardingRequest.LimitacaoRequest lim : request.limitacoes()) {
                LimitacaoFisica limitacao = new LimitacaoFisica();
                limitacao.setUser(user);
                limitacao.setRegiao(lim.regiao());
                limitacao.setDescricao(lim.descricao());
                limitacaoFisicaRepository.save(limitacao);
            }
        }

        return toResponse(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse buscarPorUserId(Long userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile não encontrado"));
        return toResponse(profile);
    }

    private ProfileResponse toResponse(Profile profile) {
        return new ProfileResponse(
                profile.getUser().getId(),
                profile.getNome(),
                profile.getDataNascimento(),
                profile.getAlturaCm(),
                profile.getObjetivo(),
                profile.getNivelExperiencia(),
                profile.getOnboardingCompleto()
        );
    }
}