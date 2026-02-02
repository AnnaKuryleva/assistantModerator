package com.example.assistantmoderator.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
