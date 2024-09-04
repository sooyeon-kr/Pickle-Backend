package com.example.pickle_customer.mystrategy.repository;

import com.example.pickle_customer.mystrategy.entity.MyStrategyCategoryComposition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryCompositionRepository extends JpaRepository<MyStrategyCategoryComposition, Integer> {
}
