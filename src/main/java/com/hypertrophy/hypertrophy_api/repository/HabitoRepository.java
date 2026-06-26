package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.Habito;
import java.util.List;

public interface HabitoRepository extends JpaRepository<Habito, Long> {

    List<Habito> findByUserIdAndAtivoTrue(Long userId);
}
