package com.example.pickle_customer.mystrategy.repository;

import com.example.pickle_customer.mystrategy.entity.MyStrategyCategoryComposition;
import com.example.pickle_customer.mystrategy.entity.MyStrategyProductComposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCompositionRepository extends JpaRepository<MyStrategyProductComposition, Integer> {
    List<MyStrategyProductComposition> findAllByCategoryComposition(MyStrategyCategoryComposition categoryComposition);
}
