package com.example.pickle_common.strategy.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.util.List;

/**
 * "pdId": PB식별ID,
 *     "customerId": 고객식별ID,
 *     "consultingHistoryId": 상담기록ID
 *     "name": "생성할 전략 이름",
 */
@Getter
public class CreateStrategyRequestDto {
    @Positive
    private int pbId;

    @Positive
    private int customerId;

    @Positive
    private int consultingHistoryId;

    private String name;

    private List<@Valid CategoryDto> categoryList;

    @Getter
    public static class CategoryDto {
        @NotBlank
        private String category;

        @Positive
        private int categoryRatio;

        private List<@Valid ProductDto> productList;
    }

    @Getter
    public static class ProductDto {
        @NotBlank
        private String code;

        @NotBlank
        private String name;

        @Positive
        private int ratio;

        @NotBlank
        private String themeName;
    }

}
