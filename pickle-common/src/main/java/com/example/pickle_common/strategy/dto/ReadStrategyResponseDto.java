package com.example.pickle_common.strategy.dto;

import com.example.real_common.global.BaseTimeEntity;
import com.example.real_common.stockEnum.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadStrategyResponseDto {
    private List<StrategyInfoDto> strategyList;

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyInfoDto {
        private int id;
        private String name;
        private String pbName;
        private String pbBranchOffice;
        private LocalDateTime createdAt;
        private List<ReadDetailStrategyResponseDto.CategoryDto> categoryList;
    }

        @Builder
        @AllArgsConstructor
        @Getter
        @NoArgsConstructor
        public static class CategoryDto {
            private String categoryName;
            private double categoryRatio;
            private List<ReadDetailStrategyResponseDto.ProductDto> productList;
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
