package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.SessaoTreino;
import com.hypertrophy.hypertrophy_api.entity.enums.StatusSessao;
import java.util.List;
import java.util.Optional;

public interface SessaoTreinoRepository extends JpaRepository<SessaoTreino, Long> {

    Optional<SessaoTreino> findByUserIdAndStatus(Long userId, StatusSessao status);

    Optional<SessaoTreino> findByIdAndUserId(Long id, Long userId);

    List<SessaoTreino> findByUserIdOrderByDataInicioDesc(Long userId);
}
