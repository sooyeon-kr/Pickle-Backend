package com.example.real_common.global.exception.error;

public class NotFoundPresetException extends RuntimeException {
    public NotFoundPresetException() {
        super();
    }

    public NotFoundPresetException(String message) {
        super(message);
    }

    public NotFoundPresetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundPresetException(Throwable cause) {
        super(cause);
    }
}
