package com.example.pickle_common.strategy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RestClientDto {

    @Getter
    public static class PbInfoRequestDto {
        @NotBlank
        private String name;
        @NotBlank
        private String branchOffice;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadStrategyResponseDto {
        private String name;
        private int strategyId;
        private List<ReadDetailStrategyResponseDto.CategoryDto> categoryList;
    }


}
