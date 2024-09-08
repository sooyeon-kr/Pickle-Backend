package com.example.real_common.global.exception.error;

public class NotFoundRequestLetterException extends RuntimeException{
    public NotFoundRequestLetterException(String message) {
        super(message);
    }
    public NotFoundRequestLetterException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundRequestLetterException(Throwable cause) {
        super(cause);
    }
    public NotFoundRequestLetterException() {
        super();
    }
}
