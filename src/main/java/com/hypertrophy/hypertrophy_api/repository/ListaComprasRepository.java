package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.ListaCompras;
import java.util.List;

public interface ListaComprasRepository extends JpaRepository<ListaCompras, Long> {

    List<ListaCompras> findByUserIdAndCompradoFalse(Long userId);

    List<ListaCompras> findByUserId(Long userId);
}
