package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.RegistroPesoMedidas;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroPesoMedidasRepository extends JpaRepository<RegistroPesoMedidas, Long> {

    List<RegistroPesoMedidas> findByUserIdOrderByDataHoraDesc(Long userId);

    List<RegistroPesoMedidas> findByUserIdAndDataHoraBetweenOrderByDataHoraAsc(
            Long userId, LocalDateTime inicio, LocalDateTime fim);

    Optional<RegistroPesoMedidas> findTopByUserIdOrderByDataHoraDesc(Long userId);
}

