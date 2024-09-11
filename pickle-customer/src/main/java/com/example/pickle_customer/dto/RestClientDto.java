package com.example.pickle_customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RestClientDto {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadCusIdResponseDto {
        private int customerId;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadCusNameResponseDto {
        private String customerName;
    }
}
