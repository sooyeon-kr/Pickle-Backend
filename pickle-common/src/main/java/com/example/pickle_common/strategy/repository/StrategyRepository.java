package com.example.pickle_common.strategy.repository;

import com.example.pickle_common.strategy.entity.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Integer> {

}
