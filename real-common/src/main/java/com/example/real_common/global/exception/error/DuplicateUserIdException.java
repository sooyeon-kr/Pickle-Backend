package com.example.real_common.global.exception.error;

public class DuplicateUserIdException extends RuntimeException{
    public DuplicateUserIdException() {super();}
    public DuplicateUserIdException(String message) {
        super(message);
    }
    public DuplicateUserIdException(String message, Throwable cause) {}
    public DuplicateUserIdException(Throwable cause) {}
}
