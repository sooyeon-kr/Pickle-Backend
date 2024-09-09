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

    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<?> handleUnAuthorizedException(UnAuthorizedException exception) {
        log.error("handleUnAuthorizedException :: ");
        ErrorCode errorCode = ErrorCode.UN_AUTHORIZED_EXCEPTION;

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

    @ExceptionHandler(NotFoundConsultingStatusException.class)
    protected ResponseEntity<?> handleNotFoundConsultingStatusException(NotFoundConsultingStatusException exception) {
        log.error("handleNotFoundConsultingStatusException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_CONSULTING_STATUS_EXCEPTION;

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

    @ExceptionHandler(NotFoundStrategyException.class)
    protected ResponseEntity<?> handleNotFoundStrategyException(NotFoundStrategyException exception) {
        log.error("handleNotFoundStrategyException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_STRATEGY_EXCEPTION;

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

    @ExceptionHandler(ConflictMyStrategyException.class)
    protected ResponseEntity<?> handleConflictMyStrategyException(ConflictMyStrategyException exception) {
        log.error("handleConflictMyStrategyException :: ");
        ErrorCode errorCode = ErrorCode.CONFLICT_MY_STRATEGY_EXCEPTION;

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

    @ExceptionHandler(NotFoundImageException.class)
    protected ResponseEntity<?> handleNotFoundImageException(NotFoundImageException exception) {
        log.error("handleNotFoundImageException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_IMAGE_EXCEPTION;
    
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

    @ExceptionHandler(NotFoundMyStrategyException.class)
    protected ResponseEntity<?> handleNotFoundMyStrategyException(NotFoundMyStrategyException exception) {
        log.error("handleNotFoundMyStrategyException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_MY_STRATEGY_EXCEPTION;

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

    @ExceptionHandler(UnableToCreateRequestLetterException.class)
    protected ResponseEntity<?> handleUnableToCreateRequestLetterException(UnableToCreateRequestLetterException exception){
        log.error("handleUnableToCreateRequestLetterException :: ");
        ErrorCode errorCode = ErrorCode.UNABLE_TO_CREATE_REQUEST_LETTER_EXCEPTION;

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
    @ExceptionHandler(NotFoundRequestLetterException.class)
    protected ResponseEntity<?> handleNotFoundRequestLetterException(NotFoundRequestLetterException exception){
        log.error("handleNotFoundRequestLetterException :: ");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_REQUEST_LETTER_EXCEPTION;

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

    @ExceptionHandler(UnableToCreateRequestLetterDuoToMqFailure.class)
    protected ResponseEntity<?> UnableToCreateRequestLetterDuoToMqFailure(UnableToCreateRequestLetterDuoToMqFailure exception){
        log.error("handleUnableToFetchMqDataException :: ");
        ErrorCode errorCode = ErrorCode.UNABLE_TO_CREATE_REQUEST_LETTER_DUE_TO_MQ_FAILURE;

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

    @ExceptionHandler(UnexpectedServiceException.class)
    protected ResponseEntity<?> handleUnexpectedServiceException(UnexpectedServiceException exception){
        log.error("handleUnexpectedServiceException :: ");
        ErrorCode errorCode = ErrorCode.UNEXPECTED_SERVICE_EXCEPTION;

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

    @ExceptionHandler(UnableToCreateRejectedInfoException.class)
    protected ResponseEntity<?> handleUnableToCreateRejectedInfoException(UnableToCreateRejectedInfoException exception){
        log.error("UnableToCreateRejectedInfoException :: ");
        ErrorCode errorCode = ErrorCode.UNABLE_TO_CREATE_REJECTED_INFO_EXCEPTION;

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

    @ExceptionHandler(UnableToSendRoomInfoToMqException.class)
    protected ResponseEntity<?> handleUnableToSendRoomInfoToMqException(UnableToSendRoomInfoToMqException exception){
        log.error("UnableToSendRoomInfoToMqException :: ");
        ErrorCode errorCode = ErrorCode.UNABLE_TO_SEND_ROOM_INFO_TO_MQ_EXCEPTION;

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
