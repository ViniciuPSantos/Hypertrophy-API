package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.RegistroSono;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroSonoRepository extends JpaRepository<RegistroSono, Long> {

    Optional<RegistroSono> findByUserIdAndData(Long userId, LocalDate data);

    List<RegistroSono> findByUserIdAndDataBetweenOrderByDataDesc(Long userId, LocalDate inicio, LocalDate fim);
}