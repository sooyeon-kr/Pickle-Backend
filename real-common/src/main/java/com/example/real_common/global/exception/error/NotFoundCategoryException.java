package com.example.real_common.global.exception.error;

public class NotFoundCategoryException extends RuntimeException {
    public NotFoundCategoryException(String message) {
        super(message);
    }
    public NotFoundCategoryException(String message, Throwable cause) {
      super(message, cause);
    }
    public NotFoundCategoryException(Throwable cause) {
      super(cause);
    }
    public NotFoundCategoryException(){
      super();
    }
}
