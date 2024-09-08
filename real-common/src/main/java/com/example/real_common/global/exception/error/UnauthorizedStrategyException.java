package com.example.real_common.global.exception.error;

public class UnauthorizedStrategyException extends RuntimeException {
    public UnauthorizedStrategyException(String message) {
        super(message);
    }
    public UnauthorizedStrategyException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnauthorizedStrategyException(Throwable cause) {
        super(cause);
    }
    public UnauthorizedStrategyException() {
        super();
    }
}
