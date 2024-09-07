package com.example.pickle_customer.order.repository;

//import com.example.pickle_customer.order.entity.ProductInAccount;
import com.example.pickle_customer.entity.ProductInAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<ProductInAccount, Long> {
    ProductInAccount findByProductCode(String productCode);

}
