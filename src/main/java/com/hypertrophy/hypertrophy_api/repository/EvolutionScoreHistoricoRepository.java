package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.EvolutionScoreHistorico;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EvolutionScoreHistoricoRepository extends JpaRepository<EvolutionScoreHistorico, Long> {

    Optional<EvolutionScoreHistorico> findByUserIdAndData(Long userId, LocalDate data);

    List<EvolutionScoreHistorico> findByUserIdAndDataBetweenOrderByDataAsc(Long userId, LocalDate inicio, LocalDate fim);

    Optional<EvolutionScoreHistorico> findTopByUserIdOrderByDataDesc(Long userId);
}

