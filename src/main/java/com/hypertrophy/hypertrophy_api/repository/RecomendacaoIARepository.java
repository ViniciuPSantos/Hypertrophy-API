package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.RecomendacaoIA;
import com.hypertrophy.hypertrophy_api.entity.enums.StatusResposta;
import java.util.List;

public interface RecomendacaoIARepository extends JpaRepository<RecomendacaoIA, Long> {

    List<RecomendacaoIA> findByUserIdOrderByDataHoraDesc(Long userId);

    List<RecomendacaoIA> findByUserIdAndStatusRespostaOrderByDataHoraDesc(Long userId, StatusResposta status);
}

