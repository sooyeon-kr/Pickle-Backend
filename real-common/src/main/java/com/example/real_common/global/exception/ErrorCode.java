package com.example.real_common.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    CUSTOM_TEST_EXCEPTION(HttpStatus.BAD_REQUEST, "TEST_001", "에러 핸들러 테스트입니다."),
    NOT_FOUND_ACCOUNT_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_001","등록된 계정이 없음"),
    NOT_FOUND_GROUP_EXCEPTION(HttpStatus.NOT_FOUND,"GROUP_001", "등록된 그룹이 없음"),
    NOT_FOUND_CONSULTING_HISTORY_EXCEPTION(HttpStatus.NOT_FOUND, "CONSULTING_HISTORY_001", "상담 기록을 찾을 수 없음"),
    NOT_FOUND_CATEGORY_EXCEPTION(HttpStatus.NOT_FOUND, "CATEGORY_001","카테고리를 찾을 없음"),
    NOT_FOUND_THEME_EXCEPTION(HttpStatus.NOT_FOUND, "THEME_001", "테마를 찾을 수 없음"),
    NOT_FOUND_PRODUCT_EXCEPTION(HttpStatus.NOT_FOUND, "PRODUCT_001", "상품을 찾을 수 없음");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
