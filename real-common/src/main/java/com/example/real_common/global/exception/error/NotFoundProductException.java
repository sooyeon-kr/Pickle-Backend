package com.example.real_common.global.exception.error;

public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException(String message) {
        super(message);
    }
    public NotFoundProductException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundProductException(Throwable cause) {
        super(cause);
    }
    public NotFoundProductException() {
        super();
    }
}
