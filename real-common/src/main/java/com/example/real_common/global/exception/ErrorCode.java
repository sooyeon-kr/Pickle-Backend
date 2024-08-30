package com.example.real_common.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    CUSTOM_TEST_EXCEPTION(HttpStatus.BAD_REQUEST, "TEST_001", "에러 핸들러 테스트입니다."),
    NOT_FOUND_ACCOUNT_EXCEPTION(HttpStatus.UNAUTHORIZED, "ACCOUNT_001","등록된 계정이 없음");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
