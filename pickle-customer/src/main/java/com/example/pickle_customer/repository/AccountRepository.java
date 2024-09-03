package com.example.pickle_customer.repository;

import com.example.pickle_customer.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    // CustomerEntity의 customerId를 통해 Account 조회
    Optional<Account> findByCustomerCustomerId(int customerId);
}
