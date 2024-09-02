package com.example.real_common.global.exception.error;

public class NotFoundConsultingHistoryException extends RuntimeException {
    public NotFoundConsultingHistoryException() {super();}
    public NotFoundConsultingHistoryException(String message) {
        super(message);
    }
    public NotFoundConsultingHistoryException(String message, Throwable cause) {super(message, cause);}
    public NotFoundConsultingHistoryException(Throwable cause) {super(cause);}
}
