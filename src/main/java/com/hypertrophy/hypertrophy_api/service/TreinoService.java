package com.hypertrophy.hypertrophy_api.service;

import com.hypertrophy.hypertrophy_api.dto.treino.FinalizarSessaoResponse;
import com.hypertrophy.hypertrophy_api.dto.treino.IniciarSessaoRequest;
import com.hypertrophy.hypertrophy_api.dto.treino.RegistrarSerieRequest;
import com.hypertrophy.hypertrophy_api.dto.treino.SessaoResponse;
import com.hypertrophy.hypertrophy_api.entity.*;
import com.hypertrophy.hypertrophy_api.entity.enums.StatusSessao;
import com.hypertrophy.hypertrophy_api.repository.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TreinoService {

    private final SessaoTreinoRepository sessaoTreinoRepository;
    private final SerieExecutadaRepository serieExecutadaRepository;
    private final ExercicioRepository exercicioRepository;
    private final PlanoTreinoRepository planoTreinoRepository;
    private final UserRepository userRepository;

    public TreinoService(SessaoTreinoRepository sessaoTreinoRepository,
                         SerieExecutadaRepository serieExecutadaRepository,
                         ExercicioRepository exercicioRepository,
                         PlanoTreinoRepository planoTreinoRepository,
                         UserRepository userRepository) {
        this.sessaoTreinoRepository = sessaoTreinoRepository;
        this.serieExecutadaRepository = serieExecutadaRepository;
        this.exercicioRepository = exercicioRepository;
        this.planoTreinoRepository = planoTreinoRepository;
        this.userRepository = userRepository;
    }

    // RF-102: iniciar sessão a partir de plano ou livre
    @Transactional
    public SessaoResponse iniciarSessao(Long userId, IniciarSessaoRequest request) {
        sessaoTreinoRepository.findByUserIdAndStatus(userId, StatusSessao.EM_ANDAMENTO)
                .ifPresent(s -> { throw new IllegalStateException("Já existe uma sessão em andamento"); });

        User user = userRepository.getReferenceById(userId);
        SessaoTreino sessao = new SessaoTreino();
        sessao.setUser(user);
        sessao.setDataInicio(LocalDateTime.now());
        sessao.setStatus(StatusSessao.EM_ANDAMENTO);

        if (request.planoTreinoId() != null) {
            planoTreinoRepository.findByIdAndUserId(request.planoTreinoId(), userId)
                    .ifPresent(sessao::setPlanoTreino);
        }

        sessao = sessaoTreinoRepository.save(sessao);
        return toSessaoResponse(sessao);
    }

    // RF-103: registrar série durante execução
    @Transactional
    public void registrarSerie(Long userId, Long sessaoId, RegistrarSerieRequest request) {
        SessaoTreino sessao = sessaoTreinoRepository.findByIdAndUserId(sessaoId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));

        if (sessao.getStatus() != StatusSessao.EM_ANDAMENTO) {
            throw new IllegalStateException("Sessão não está em andamento");
        }

        SerieExecutada serie = new SerieExecutada();
        serie.setSessaoTreino(sessao);
        serie.setExercicio(exercicioRepository.getReferenceById(request.exercicioId()));
        serie.setNumeroSerie(request.numeroSerie());
        serie.setCargaKg(request.cargaKg());
        serie.setRepeticoesRealizadas(request.repeticoesRealizadas());
        serie.setRpe(request.rpe());
        serie.setObservacao(request.observacao());

        // RN-104: edição via versão — preserva original
        if (request.versaoAnteriorId() != null) {
            serieExecutadaRepository.findById(request.versaoAnteriorId())
                    .ifPresent(serie::setVersaoAnterior);
        }

        serieExecutadaRepository.save(serie);
    }

    // RF-105 + dispara avaliação de deload/platô assíncrona (RN-102)
    @Transactional
    public FinalizarSessaoResponse finalizarSessao(Long userId, Long sessaoId) {
        SessaoTreino sessao = sessaoTreinoRepository.findByIdAndUserId(sessaoId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));

        if (sessao.getStatus() != StatusSessao.EM_ANDAMENTO) {
            throw new IllegalStateException("Sessão não está em andamento");
        }

        LocalDateTime fim = LocalDateTime.now();
        int duracaoSegundos = (int) ChronoUnit.SECONDS.between(sessao.getDataInicio(), fim);

        sessao.setDataFim(fim);
        sessao.setStatus(StatusSessao.FINALIZADA);
        sessao.setDuracaoSegundos(duracaoSegundos);
        sessaoTreinoRepository.save(sessao);

        List<SerieExecutada> series = serieExecutadaRepository
                .findBySessaoTreinoIdOrderByExercicioIdAscNumeroSerieAsc(sessaoId);

        List<String> prs = detectarRecordesPessoais(userId, series);

        // Avaliação de platô/deload dispara de forma assíncrona — não bloqueia a resposta (RF-4.3)
        avaliarFadigaAsync(userId, sessaoId);

        return new FinalizarSessaoResponse(
                sessaoId,
                duracaoSegundos,
                series.size(),
                prs,
                gerarResumoTexto(series.size(), prs, duracaoSegundos)
        );
    }

    @Transactional
    public void abandonarSessao(Long userId, Long sessaoId) {
        SessaoTreino sessao = sessaoTreinoRepository.findByIdAndUserId(sessaoId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));

        sessao.setStatus(StatusSessao.ABANDONADA);
        sessao.setDataFim(LocalDateTime.now());
        sessaoTreinoRepository.save(sessao);
    }

    private List<String> detectarRecordesPessoais(Long userId, List<SerieExecutada> series) {
        // TODO: comparar carga máxima atual com histórico para detectar PRs (RF-105)
        return List.of();
    }

    private String gerarResumoTexto(int totalSeries, List<String> prs, int duracaoSegundos) {
        int minutos = duracaoSegundos / 60;
        String base = String.format("Treino finalizado: %d séries em %d minutos.", totalSeries, minutos);
        if (!prs.isEmpty()) {
            base += " Novos recordes: " + String.join(", ", prs) + "!";
        }
        return base;
    }

    @Async
    protected void avaliarFadigaAsync(Long userId, Long sessaoId) {
        // TODO: implementar RN-102 (detecção de deload) — depende de histórico acumulado (P2 no backlog)
    }

    private SessaoResponse toSessaoResponse(SessaoTreino sessao) {
        return new SessaoResponse(
                sessao.getId(),
                sessao.getPlanoTreino() != null ? sessao.getPlanoTreino().getId() : null,
                sessao.getDataInicio(),
                sessao.getStatus()
        );
    }
}