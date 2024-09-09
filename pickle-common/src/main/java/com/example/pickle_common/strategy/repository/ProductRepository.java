package com.example.pickle_common.strategy.repository;

import com.example.pickle_common.strategy.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByCode(String code);

//    @Query("SELECT p FROM Product p WHERE " +
//            "(:name IS NULL OR p.name LIKE %:name%) AND " +
//            "(:themeName IS NULL OR p.themeName LIKE %:themeName%) AND " +
//            "(:categoryName IS NULL OR p.categoryName LIKE %:categoryName%)")
//    List<Product> findByFilters(@Param("name") String name,
//                                @Param("themeName") String themeName,
//                                @Param("categoryName") String categoryName);

}
