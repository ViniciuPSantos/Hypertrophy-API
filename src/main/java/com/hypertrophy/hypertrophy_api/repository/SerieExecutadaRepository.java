package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.hypertrophy.hypertrophy_api.entity.SerieExecutada;
import java.util.List;

public interface SerieExecutadaRepository extends JpaRepository<SerieExecutada, Long> {

    List<SerieExecutada> findBySessaoTreinoIdOrderByExercicioIdAscNumeroSerieAsc(Long sessaoTreinoId);

    // Últimas N execuções de um exercício por usuário (para calcular progressão — RN-101)
    @Query("""
            SELECT s FROM SerieExecutada s
            JOIN s.sessaoTreino st
            WHERE st.user.id = :userId
              AND s.exercicio.id = :exercicioId
              AND st.status = 'FINALIZADA'
              AND s.versaoAnterior IS NULL
            ORDER BY st.dataInicio DESC
            LIMIT :limite
            """)
    List<SerieExecutada> findUltimasExecucoes(Long userId, Long exercicioId, int limite);
}