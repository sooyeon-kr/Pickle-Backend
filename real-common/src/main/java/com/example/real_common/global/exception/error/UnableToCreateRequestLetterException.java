package com.example.real_common.global.exception.error;

public class UnableToCreateRequestLetterException extends RuntimeException{
    public UnableToCreateRequestLetterException(String message) {
        super(message);
    }
    public UnableToCreateRequestLetterException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnableToCreateRequestLetterException(Throwable cause) {
        super(cause);
    }
    public UnableToCreateRequestLetterException() {
        super();
    }
}
