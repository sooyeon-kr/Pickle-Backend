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
    //TODO: image 추가에 따른 로직 추가
    private String pbImage;
    private LocalDateTime date;
    private LocalDateTime createdAt;
    private ConsultingStatusEnum status;
    private ConsultingRejectInfoDto consultingRejectInfo;
}


