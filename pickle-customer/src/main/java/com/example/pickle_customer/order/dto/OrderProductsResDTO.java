package com.example.pickle_customer.order.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OrderProductsResDTO {
    private String categoryName;
    private double categoryRatio;
    private List<ProductDto> productList;

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class ProductDto {
        private String code;
        private String name;
        private double ratio;
        private double myStrategyRatio;

    }
}