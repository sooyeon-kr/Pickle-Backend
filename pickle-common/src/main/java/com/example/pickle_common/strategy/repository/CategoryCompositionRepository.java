package com.example.pickle_common.strategy.repository;

import com.example.pickle_common.strategy.entity.CategoryComposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryCompositionRepository extends JpaRepository<CategoryComposition, Integer> {
    List<CategoryComposition> findByStrategy_strategyId(Integer strategyId);
}
