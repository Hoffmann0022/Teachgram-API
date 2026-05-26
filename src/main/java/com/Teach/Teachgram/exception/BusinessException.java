package com.Teach.Teachgram.exception;

// Lançada para violações de regra de negócio (400)
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
