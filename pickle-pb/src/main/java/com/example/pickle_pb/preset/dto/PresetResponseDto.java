package com.example.pickle_pb.preset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PresetResponseDto {
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class ReadPresetListResponseDto {
        private List<PresetDto> presetList;

        @Getter
        @AllArgsConstructor
        @Builder
        @NoArgsConstructor
        public static class PresetDto {
            private Integer presetId;
            private Integer groupId;
            private String name;
            private List<CategoryDto> categoryList;
        }

        @Getter
        @AllArgsConstructor
        @Builder
        @NoArgsConstructor
        public static class CategoryDto {
            private String categoryName;
            private double categoryRatio;
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class ReadPresetDetailResponseDto {
        private Integer presetId;
        private Integer groupId;
        private String name;
        private List<CategoryDto> presetList;

        @Getter
        @AllArgsConstructor
        @Builder
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

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class CreatePresetResponseDto {
        Integer presetId;
    }
}
