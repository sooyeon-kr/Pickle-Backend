package com.example.pickle_common.search.specification;

import com.example.pickle_common.strategy.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class ProductSpecification {

    public static Specification<Product> containingName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> containingCategoryAndName(String categoryName, String name) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate categoryPredicate = criteriaBuilder.like(root.get("categoryName"), "%" + categoryName + "%");
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + name + "%");

            return criteriaBuilder.and(categoryPredicate, namePredicate);
        };
    }

    public static Specification<Product> containingCategoryAndThemeAndName(String categoryName, String themeName, String name) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate categoryPredicate = criteriaBuilder.like(root.get("categoryName"), "%" + categoryName + "%");
            Predicate themePredicate = criteriaBuilder.like(root.get("themeName"), "%" + themeName + "%");
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + name + "%");

            return criteriaBuilder.and(categoryPredicate, themePredicate, namePredicate);
        };
    }
}
