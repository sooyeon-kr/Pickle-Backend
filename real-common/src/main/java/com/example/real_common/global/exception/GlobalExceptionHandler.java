package com.example.real_common.global.exception;

import com.example.real_common.global.exception.dto.CommonResponse;
import com.example.real_common.global.exception.dto.ErrorResponse;
import com.example.real_common.global.exception.error.CustomTestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomTestException.class)
    protected ResponseEntity<?> handleCustomTestException(CustomTestException exception) {
        log.error("handleCustomTestException :: ");

        ErrorCode errorCode = ErrorCode.CUSTOM_TEST_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}