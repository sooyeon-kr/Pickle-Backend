package com.example.real_common.global.exception.error;

public class NotFoundThemeException extends RuntimeException {
    public NotFoundThemeException(String message) {
        super(message);
    }
    public NotFoundThemeException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundThemeException(Throwable cause) {
        super(cause);
    }
    public NotFoundThemeException() {
        super();
    }
}
