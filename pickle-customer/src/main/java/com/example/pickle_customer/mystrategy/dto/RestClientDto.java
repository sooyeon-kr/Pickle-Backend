package com.example.pickle_customer.mystrategy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RestClientDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadStrategyResponseDto {
        private String name;
        private int strategyId;
        private List<CategoryDto> categoryList;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class CategoryDto {
        private String categoryName;
        private double categoryRatio;
        private List<ProductDto> productList;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class ProductDto {
        private String code;
        private String name;
        private String themeName;
        private double ratio;
    }
}
