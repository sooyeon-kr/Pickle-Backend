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
        private String name;
        private String pbName;
        private String pbBranchOffice;
        private LocalDateTime createdAt;
        private List<String> categoryComposition;
    }
}
