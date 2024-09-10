package com.example.pickle_customer.order.dto;

import com.example.pickle_customer.mystrategy.dto.RestClientDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioResDTO {
    private String portfolioName;
    private List<CategoryDTO> categoryDTOs;
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryDTO {
        private String name;
        private double ratio;
        private double totalPurchaseAmount;
        private List<ProductDTO> productDtos;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductDTO {
        private String name;
        private String code;
        private double quantity;
        private double ratio;
        private String themeName;
        private double purchaseAmount;
        private String categoryName;


    }

}
