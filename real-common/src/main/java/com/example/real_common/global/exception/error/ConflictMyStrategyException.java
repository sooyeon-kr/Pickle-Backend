package com.example.real_common.global.exception.error;

public class ConflictMyStrategyException extends RuntimeException {
    public ConflictMyStrategyException(String message) {
        super(message);
    }
    public ConflictMyStrategyException(String message, Throwable cause) {
        super(message, cause);
    }
    public ConflictMyStrategyException(Throwable cause) {
        super(cause);
    }
    public ConflictMyStrategyException() {
        super();
    }
}
