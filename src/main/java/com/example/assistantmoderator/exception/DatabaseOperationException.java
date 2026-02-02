package com.example.assistantmoderator.exception;

public class DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message, Throwable ex) {
        super(message, ex);
    }
}
