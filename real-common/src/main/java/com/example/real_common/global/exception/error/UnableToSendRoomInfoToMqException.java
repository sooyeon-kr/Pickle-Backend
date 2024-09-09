package com.example.real_common.global.exception.error;

public class UnableToSendRoomInfoToMqException extends RuntimeException{
    public UnableToSendRoomInfoToMqException() {
        super();
    }
    public UnableToSendRoomInfoToMqException(String message) {
        super(message);
    }
    public UnableToSendRoomInfoToMqException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnableToSendRoomInfoToMqException(Throwable cause) {
        super(cause);
    }
}
