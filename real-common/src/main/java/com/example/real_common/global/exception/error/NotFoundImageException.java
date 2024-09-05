package com.example.real_common.global.exception.error;

public class NotFoundImageException extends RuntimeException {
    public NotFoundImageException() {
        super();
    }

    public NotFoundImageException(String message) {
        super(message);
    }

    public NotFoundImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundImageException(Throwable cause) {
        super(cause);
    }
}
