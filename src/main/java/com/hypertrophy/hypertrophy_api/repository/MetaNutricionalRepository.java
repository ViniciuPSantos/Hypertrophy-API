package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.MetaNutricional;
import java.util.Optional;

public interface MetaNutricionalRepository extends JpaRepository<MetaNutricional, Long> {

    Optional<MetaNutricional> findTopByUserIdOrderByDataVigenciaInicioDesc(Long userId);
}

