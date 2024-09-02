package com.example.pickle_customer.repository;

import com.example.pickle_customer.entity.CustomerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByUserId(String userid);
    Optional<CustomerEntity> findByName(String username);

    @Query("SELECT COALESCE(MAX(c.mydataId), 0) FROM CustomerEntity c")
    Optional<Integer> findMaxMydataId();
}
