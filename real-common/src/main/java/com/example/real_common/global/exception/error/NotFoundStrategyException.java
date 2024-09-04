package com.example.real_common.global.exception.error;

public class NotFoundStrategyException extends RuntimeException {
    public NotFoundStrategyException(String message) {
        super(message);
    }
    public NotFoundStrategyException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundStrategyException(Throwable cause) {
        super(cause);
    }
    public NotFoundStrategyException() {
        super();
    }
}
