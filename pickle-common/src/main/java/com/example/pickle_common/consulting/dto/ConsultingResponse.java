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
public class ConsultingResponse {
    private int requestLetterId;
    private int pbId;
    private String pbName;
    private LocalDateTime date;
    private LocalDateTime createdAt;
    private ConsultingStatusEnum status;
    private ConsultingRejectInfoDto consultingRejectInfo;
}


