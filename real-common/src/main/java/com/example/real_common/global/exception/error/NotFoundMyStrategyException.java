package com.example.real_common.global.exception.error;

public class NotFoundMyStrategyException extends RuntimeException {
    public NotFoundMyStrategyException(String message) {
        super(message);
    }
    public NotFoundMyStrategyException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundMyStrategyException(Throwable cause) {
        super(cause);
    }
    public NotFoundMyStrategyException() {
        super();
    }
}
