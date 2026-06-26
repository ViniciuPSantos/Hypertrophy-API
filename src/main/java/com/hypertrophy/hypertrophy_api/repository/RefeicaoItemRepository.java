package com.hypertrophy.hypertrophy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hypertrophy.hypertrophy_api.entity.RefeicaoItem;
import java.util.List;

public interface RefeicaoItemRepository extends JpaRepository<RefeicaoItem, Long> {

    List<RefeicaoItem> findByRefeicaoId(Long refeicaoId);
}
