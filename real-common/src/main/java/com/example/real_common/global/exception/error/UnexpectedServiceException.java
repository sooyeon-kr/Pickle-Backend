package com.example.real_common.global.exception.error;

public class UnexpectedServiceException extends RuntimeException{
    public UnexpectedServiceException() {
        super();
    }
    public UnexpectedServiceException(String message) {
        super(message);
    }
    public UnexpectedServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnexpectedServiceException(Throwable cause) {
        super(cause);
    }
}
