package com.example.pickle_common.strategy.repository;

import com.example.pickle_common.strategy.entity.ProductComposition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCompositionRepository extends JpaRepository<ProductComposition, Integer> {
}
