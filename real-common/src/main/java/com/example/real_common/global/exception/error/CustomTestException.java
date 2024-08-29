package com.example.real_common.global.exception.error;

public class CustomTestException extends RuntimeException {
    public CustomTestException() {
        super();
    }

    public CustomTestException(String message) {
        super(message);
    }

    public CustomTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomTestException(Throwable cause) {
        super(cause);
    }
}
