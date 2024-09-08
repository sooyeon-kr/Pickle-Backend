package com.example.real_common.global.exception.error;

public class UnableToCreateRejectedInfoException extends RuntimeException{
    public UnableToCreateRejectedInfoException(String message) {
        super(message);
    }
    public UnableToCreateRejectedInfoException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnableToCreateRejectedInfoException(Throwable cause) {
        super(cause);
    }
    public UnableToCreateRejectedInfoException() {
        super();
    }
}
