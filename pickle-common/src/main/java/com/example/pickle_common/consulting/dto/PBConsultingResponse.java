package com.example.pickle_common.consulting.dto;

import com.example.pickle_common.consulting.entity.ConsultingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PBConsultingResponse {
    private int requestLetterId;
    private int customerId;
    private String customerName;
    private LocalDateTime date;
    private LocalDateTime createdAt;
    private ConsultingStatusEnum status;
    private ConsultingRejectInfoDto consultingRejectInfo;
}


