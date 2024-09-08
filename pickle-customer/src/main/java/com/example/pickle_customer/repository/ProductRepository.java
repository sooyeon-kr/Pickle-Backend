package com.example.pickle_customer.repository;


import com.example.pickle_customer.entity.Account;
import com.example.pickle_customer.entity.ProductInAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductInAccount, Integer> {
    List<ProductInAccount> findByAccountAccountId(int accountId);
    void deleteByAccount(Account account);

    ProductInAccount findByProductCodeAndAccount(String productCode, Account account);
}
