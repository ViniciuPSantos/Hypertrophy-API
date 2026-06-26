package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.ContextoTreino;
import java.util.List;
import java.util.Optional;

public interface ContextoTreinoRepository extends JpaRepository<ContextoTreino, Long> {

    List<ContextoTreino> findByUserId(Long userId);

    Optional<ContextoTreino> findByUserIdAndAtivoTrue(Long userId);
}
