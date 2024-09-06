package com.example.pickle_common.consulting.entity;

import com.example.real_common.global.exception.error.NotFoundConsultingStatusException;
import lombok.Getter;

@Getter
public enum ConsultingStatusEnum {
    ETC(0, "기타"),
    REQUESTED(1, "상담요청"),
    ACCEPTED(2, "상담수락"),
    REJECTED(3, "상담거절"),
    COMPLETED(4, "상담완료"),
    NO_SHOW(5, "상담노쇼"),;

    private final int code;
    private final String descriptiong;

    private ConsultingStatusEnum(int code, String descriptiong) {
        this.code = code;
        this.descriptiong = descriptiong;
    }

    public static ConsultingStatusEnum fromCode(int code) {
        for (ConsultingStatusEnum status : ConsultingStatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return ConsultingStatusEnum.ETC;
    }
}
