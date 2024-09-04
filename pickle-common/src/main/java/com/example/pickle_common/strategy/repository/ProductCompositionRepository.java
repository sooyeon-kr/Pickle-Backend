package com.example.pickle_common.strategy.repository;

import com.example.pickle_common.strategy.entity.ProductComposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCompositionRepository extends JpaRepository<ProductComposition, Integer> {
    List<ProductComposition> findAllByCategoryComposition_Id(Integer categoryCompositionId);
}
