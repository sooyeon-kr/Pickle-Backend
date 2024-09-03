package com.example.real_common.global.exception;

import com.example.real_common.global.exception.dto.CommonResponse;
import com.example.real_common.global.exception.dto.ErrorResponse;
import com.example.real_common.global.exception.error.*;
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

    @ExceptionHandler(NotFoundGroupException.class)
    protected ResponseEntity<?> handleNotFoundGroupException(NotFoundGroupException exception) {
        log.error("handleNotFoundGroupException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_GROUP_EXCEPTION;

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

    @ExceptionHandler(NotFoundAccountException.class)
    protected ResponseEntity<?> handleNotFoundAccountException(NotFoundAccountException exception) {
        log.error("handleNotFoundAccountException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_ACCOUNT_EXCEPTION;

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

    @ExceptionHandler(NotFoundCategoryException.class)
    protected ResponseEntity<?> handleNotFoundCategoryException(NotFoundCategoryException exception) {
        log.error("handleNotFoundCategoryException :: ");

        ErrorCode errorCode = ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION;

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

    @ExceptionHandler(NotFoundThemeException.class)
    protected ResponseEntity<?> handleNotFoundThemeException(NotFoundThemeException exception) {
        log.error("handleNotFoundThemeException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_THEME_EXCEPTION;

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

    @ExceptionHandler(NotFoundProductException.class)
    protected ResponseEntity<?> handleNotFoundProductException(NotFoundProductException exception) {
        log.error("handleNotFoundProductException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_PRODUCT_EXCEPTION;

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

    @ExceptionHandler(NotFoundConsultingHistoryException.class)
    protected ResponseEntity<?> handleNotFoundConsultingHistoryException(NotFoundConsultingHistoryException exception) {
        log.error("handleNotFoundConsultingHistoryException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_CONSULTING_HISTORY_EXCEPTION;

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