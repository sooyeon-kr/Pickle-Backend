package com.example.pickle_customer.rebalancing.repository;

//import com.example.pickle_customer.rebalancing.entity.ProductInAccount;
import com.example.pickle_customer.entity.ProductInAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.List;

public interface RebalancingRepository extends JpaRepository<ProductInAccount, Long> {
    List<ProductInAccount> findByProductCode(String productCode);

}
