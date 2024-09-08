package com.example.real_common.global.exception.error;

public class IllegalArgumentAmountException extends RuntimeException {
    public IllegalArgumentAmountException(String message) {
        super(message);
    }
    public IllegalArgumentAmountException(String message, Throwable cause) {
      super(message, cause);
    }
    public IllegalArgumentAmountException(Throwable cause) {
      super(cause);
    }
    public IllegalArgumentAmountException() {
      super();
    }
}
