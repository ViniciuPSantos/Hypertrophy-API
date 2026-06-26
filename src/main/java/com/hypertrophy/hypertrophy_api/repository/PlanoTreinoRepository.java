package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.PlanoTreino;
import java.util.Optional;

public interface PlanoTreinoRepository extends JpaRepository<PlanoTreino, Long> {

    Optional<PlanoTreino> findByUserIdAndAtivoTrue(Long userId);

    Optional<PlanoTreino> findByIdAndUserId(Long id, Long userId);
}
