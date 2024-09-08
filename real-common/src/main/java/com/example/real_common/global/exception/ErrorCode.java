package com.example.real_common.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    CUSTOM_TEST_EXCEPTION(HttpStatus.BAD_REQUEST, "TEST_001", "에러 핸들러 테스트입니다."),
    NOT_FOUND_ACCOUNT_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_001", "등록된 계정이 없음"),
    NOT_FOUND_GROUP_EXCEPTION(HttpStatus.NOT_FOUND, "GROUP_001", "등록된 그룹이 없음"),
    NOT_FOUND_CONSULTING_HISTORY_EXCEPTION(HttpStatus.NOT_FOUND, "CONSULTING_HISTORY_001", "상담 기록을 찾을 수 없음"),
    NOT_FOUND_CATEGORY_EXCEPTION(HttpStatus.NOT_FOUND, "CATEGORY_001", "카테고리를 찾을 없음"),
    NOT_FOUND_THEME_EXCEPTION(HttpStatus.NOT_FOUND, "THEME_001", "테마를 찾을 수 없음"),
    NOT_FOUND_PRODUCT_EXCEPTION(HttpStatus.NOT_FOUND, "PRODUCT_001", "상품을 찾을 수 없음"),
    NOT_FOUND_CONSULTING_STATUS_EXCEPTION(HttpStatus.NOT_FOUND, "CONSULTING_001", "상담 상태를 찾을 수 없음"),
    UN_AUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_002", "사용자의 권한이 아님"),
    NOT_FOUND_STRATEGY_EXCEPTION(HttpStatus.NOT_FOUND, "STRATEGY_001", "전략을 찾을 수 없음"),
    NOT_FOUND_MY_STRATEGY_EXCEPTION(HttpStatus.NOT_FOUND, "MY_STRATEGY_001", "나의 전략을 찾을 수 없음"),
    NOT_FOUND_PRESET_EXCEPTION(HttpStatus.NOT_FOUND, "PRESET_001", "프리셋을 찾을 수 없습니다."),
    CONFLICT_MY_STRATEGY_EXCEPTION(HttpStatus.CONFLICT, "MY_STRATEGY_002", "이미 나의 전략이 존재함"),
    NOT_FOUND_IMAGE_EXCEPTION(HttpStatus.NOT_FOUND, "IMAGE_001", "사진이 없습니다."),
    UNAUTHORIZED_STRATEGY_EXCEPTION(HttpStatus.UNAUTHORIZED, "STRATEGY_002", "접근할 권한이 없는 전략임"),

    ILLEGAL_ARGUMENT_AMOUNT_EXCEPTION(HttpStatus.BAD_REQUEST, "AMOUNT_001", "잔액 이상의 값을 매매할 수 없음");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
