package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.LimitacaoFisica;
import java.util.List;

public interface LimitacaoFisicaRepository extends JpaRepository<LimitacaoFisica, Long> {

    List<LimitacaoFisica> findByUserIdAndAtivoTrue(Long userId);
}