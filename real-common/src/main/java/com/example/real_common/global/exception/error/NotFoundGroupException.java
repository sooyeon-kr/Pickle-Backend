package com.example.real_common.global.exception.error;

public class NotFoundGroupException extends RuntimeException {
    public NotFoundGroupException() {
        super();
    }
    public NotFoundGroupException(String message) {
        super(message);
    }
    public NotFoundGroupException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundGroupException(Throwable cause) {
        super(cause);
    }
}
