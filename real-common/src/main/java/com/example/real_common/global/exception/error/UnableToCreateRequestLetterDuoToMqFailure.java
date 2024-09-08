package com.example.real_common.global.exception.error;

public class UnableToCreateRequestLetterDuoToMqFailure extends RuntimeException{
    public UnableToCreateRequestLetterDuoToMqFailure() {
        super();
    }
    public UnableToCreateRequestLetterDuoToMqFailure(String message) {
        super(message);
    }
    public UnableToCreateRequestLetterDuoToMqFailure(String message, Throwable cause) {
        super(message, cause);
    }
    public UnableToCreateRequestLetterDuoToMqFailure(Throwable cause) {
        super(cause);
    }
}
