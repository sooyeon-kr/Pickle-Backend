package com.example.pickle_common.consulting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateRequestLetterRequestDto {
    private LocalDateTime date; // 상담일자
    private String request;

    private int answer1;
    private int answer2;
    private int answer3;
    private int answer4;
    private int availableInvestAmount;
    private int desiredInvestAmount;
    private int monthlyIncome;

    private CustomerInfo customerInfo;
    private PbInfo pbInfo;

    private String referenceFileUrl; // S3 URL

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class CustomerInfo {
        private int customerAge;
        private int customerGender;
        private String customerJob;
    }

    // Nested class for pb info
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class PbInfo {
        private String pbNumber;
        private String name;
//        private String img;
        private String branchOffice;
    }
}
