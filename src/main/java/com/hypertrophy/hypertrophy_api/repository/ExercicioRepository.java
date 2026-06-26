package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.Exercicio;
import com.hypertrophy.hypertrophy_api.entity.enums.GrupoMuscular;
import com.hypertrophy.hypertrophy_api.entity.enums.Equipamento;
import java.util.List;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    List<Exercicio> findByGrupoMuscularPrimario(GrupoMuscular grupoMuscular);

    List<Exercicio> findByEquipamentoNecessarioIn(List<Equipamento> equipamentos);
}