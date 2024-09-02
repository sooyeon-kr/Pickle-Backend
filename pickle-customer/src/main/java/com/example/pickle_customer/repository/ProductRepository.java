package com.example.pickle_customer.repository;


import com.example.pickle_customer.entity.ProductInAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductInAccount, Integer> {
    // 특정 accountId로 관련된 모든 Product를 찾는 메소드
    List<ProductInAccount> findByAccountAccountId(int accountId);
}
