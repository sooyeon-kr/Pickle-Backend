package com.example.pickle_customer.mystrategy.repository;

import com.example.pickle_customer.mystrategy.entity.MyStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyStrategyRepository extends JpaRepository<MyStrategy, Integer> {
}
