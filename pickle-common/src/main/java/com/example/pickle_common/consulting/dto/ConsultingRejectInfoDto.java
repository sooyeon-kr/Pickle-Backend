package com.example.pickle_common.consulting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultingRejectInfoDto {
    private String content;
    private LocalDateTime date;
}

