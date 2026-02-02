package com.example.assistantmoderator.exception;

/**
 * Исключение для ошибок авторизации во внешнем сервисе GigaChat.
 */
public class GigaChatAuthException extends RuntimeException {
    public GigaChatAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
