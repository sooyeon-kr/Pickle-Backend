package com.example.real_common.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    CUSTOM_TEST_EXCEPTION(HttpStatus.BAD_REQUEST, "TEST_001", "에러 핸들러 테스트입니다.");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
