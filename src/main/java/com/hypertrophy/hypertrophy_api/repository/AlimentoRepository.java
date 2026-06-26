package com.hypertrophy.hypertrophy_api.repository;

import com.hypertrophy.hypertrophy_api.entity.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

    List<Alimento> findByNomeContainingIgnoreCase(String nome);
}
