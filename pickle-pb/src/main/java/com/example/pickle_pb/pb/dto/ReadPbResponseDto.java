package com.example.pickle_pb.pb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReadPbResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class InfoForStrategyDto {
        private String name;
        private String branchOffice;
    }
}
