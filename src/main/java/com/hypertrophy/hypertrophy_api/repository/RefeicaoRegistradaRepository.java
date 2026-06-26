package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.RefeicaoRegistrada;
import java.time.LocalDateTime;
import java.util.List;

public interface RefeicaoRegistradaRepository extends JpaRepository<RefeicaoRegistrada, Long> {

    List<RefeicaoRegistrada> findByUserIdAndDataHoraBetweenOrderByDataHoraAsc(
            Long userId, LocalDateTime inicio, LocalDateTime fim);
}
