package com.example.pickle_common.search.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchProductDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Response {
        private String query;
        private List<ProductDto> items;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ProductDto {
        private String code;
        private String name;
        private String imgUrl;
        private String themeName;
        private String categoryName;
    }
}
