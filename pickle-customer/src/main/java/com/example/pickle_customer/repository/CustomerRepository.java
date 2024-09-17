package com.example.pickle_customer.repository;

import com.example.pickle_customer.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUserId(String userId);
    Optional<Customer> findByCustomerId(int customerId);
    Optional<Customer> findByName(String username);

    @Query("SELECT COALESCE(MAX(c.mydataId), 0) FROM Customer c")
    Optional<Integer> findMaxMydataId();
}
