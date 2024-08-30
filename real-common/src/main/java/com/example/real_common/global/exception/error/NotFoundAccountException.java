package com.example.real_common.global.exception.error;

public class NotFoundAccountException extends RuntimeException {
    public NotFoundAccountException() {
        super();
    }

    public NotFoundAccountException(String message) {
        super(message);
    }

    public NotFoundAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAccountException(Throwable cause) {
        super(cause);
    }

}
