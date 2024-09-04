package com.example.pickle_common.strategy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadDetailStrategyResponseDto {
    private String name;
    private LocalDateTime createdAt;
    private List<CategoryDto> categoryList;

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