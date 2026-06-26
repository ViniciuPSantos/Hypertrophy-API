package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.EstoqueCozinha;
import java.util.List;
import java.util.Optional;

public interface EstoqueCozinhaRepository extends JpaRepository<EstoqueCozinha, Long> {

    List<EstoqueCozinha> findByUserId(Long userId);

    Optional<EstoqueCozinha> findByUserIdAndAlimentoId(Long userId, Long alimentoId);

    Optional<EstoqueCozinha> findByIdAndUserId(Long id, Long userId);
}