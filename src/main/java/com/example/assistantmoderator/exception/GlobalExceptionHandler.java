package com.example.assistantmoderator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
    Где возникает: при получении ответа от GigaChat, если содержимое (content) не является валидным JSON.
    Что возвращает: 502 Bad Gateway со структурой {status: 502, error: "Bad Gateway", message: "..."}.
    Когда использовать: когда вышестоящий сервис (GigaChat) прислал ответ, который мы не смогли прочитать.
    */
    @ExceptionHandler(ParsingResponseGigaChatException.class)
    public ResponseEntity<Map<String, Object>> handleParsingResponseGigaChatException(ParsingResponseGigaChatException e) {
        log.error("Ошибка обработки ответа от GigaChat: {}", e.getMessage(), e);
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_GATEWAY.value());
        errors.put("error", "Bad Gateway");
        errors.put("message", "Внешний сервис модерации временно недоступен или вернул некорректный ответ. " +
                "Пожалуйста, повторите попытку позже.");
        return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
    }

    /*
    Где возникает: любые непредвиденные ошибки во время выполнения программы.
    Что возвращает: 500 Internal Server Error.
    */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        log.error("Непредвиденная системная ошибка: {}", e.getMessage(), e);
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("error", "Internal Server Error");
        errors.put("message", "Внутренняя ошибка сервера");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
