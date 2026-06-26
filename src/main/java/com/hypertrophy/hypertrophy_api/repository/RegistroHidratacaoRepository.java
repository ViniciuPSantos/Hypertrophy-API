package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.hypertrophy.hypertrophy_api.entity.RegistroHidratacao;
import java.time.LocalDateTime;
import java.util.List;

public interface RegistroHidratacaoRepository extends JpaRepository<RegistroHidratacao, Long> {

    List<RegistroHidratacao> findByUserIdAndDataHoraBetween(Long userId, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT COALESCE(SUM(r.quantidadeMl), 0) FROM RegistroHidratacao r WHERE r.user.id = :userId AND r.dataHora BETWEEN :inicio AND :fim")
    java.math.BigDecimal sumQuantidadeMlByUserIdAndDataHoraBetween(Long userId, LocalDateTime inicio, LocalDateTime fim);
}
