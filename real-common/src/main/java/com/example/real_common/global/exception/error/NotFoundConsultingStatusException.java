package com.example.real_common.global.exception.error;

public class NotFoundConsultingStatusException extends RuntimeException {
    public NotFoundConsultingStatusException() {
        super();
    }
    public NotFoundConsultingStatusException(String message) {
        super(message);
    }
    public NotFoundConsultingStatusException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundConsultingStatusException(Throwable cause) {
        super(cause);
    }
}
