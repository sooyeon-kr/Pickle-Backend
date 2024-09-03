package com.example.pickle_common.strategy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class RestClientDto {

    @Getter
    public static class PbInfoRequestDto {
        @NotBlank
        private String name;
        @NotBlank
        private String branchOffice;
    }
}
