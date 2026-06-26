package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.PlanoTreinoExercicio;
import java.util.List;

public interface PlanoTreinoExercicioRepository extends JpaRepository<PlanoTreinoExercicio, Long> {

    List<PlanoTreinoExercicio> findByPlanoTreinoIdOrderByDiaDaSemanaAscOrdemAsc(Long planoTreinoId);
}