package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.HabitoRegistro;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitoRegistroRepository extends JpaRepository<HabitoRegistro, Long> {

    List<HabitoRegistro> findByHabitoIdAndDataBetween(Long habitoId, LocalDate inicio, LocalDate fim);

    Optional<HabitoRegistro> findByHabitoIdAndData(Long habitoId, LocalDate data);
}