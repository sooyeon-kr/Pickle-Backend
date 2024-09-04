package com.example.pickle_customer.mystrategy.repository;

import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.mystrategy.entity.MyStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyStrategyRepository extends JpaRepository<MyStrategy, Integer> {
    Optional<MyStrategy> findByAccount(Account account);
}
