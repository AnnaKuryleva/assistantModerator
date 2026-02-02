package com.example.assistantmoderator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка ошибок парсинга JSON при десериализации ответов внешнего API.
     * Возникает, если формат ответа GigaChat не соответствует ожидаемой DTO.
     *
     * @param e исключениеParsingResponseGigaChatException
     * @return ResponseEntity со статусом 502 Bad Gateway
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

    /**
     * Обработка ошибок аутентификации во внешних сервисах.
     * Вызывается при невозможности получения токена доступа (OAuth2).
     *
     * @param e исключение GigaChatAuthException
     * @return ResponseEntity со статусом 502 Bad Gateway
     */
    @ExceptionHandler(GigaChatAuthException.class)
    public ResponseEntity<Map<String, Object>> handleGigaChatAuthException(GigaChatAuthException e) {
        log.error("Критическая ошибка авторизации: {}", e.getMessage(), e);
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_GATEWAY.value());
        errors.put("error", "Bad Gateway");
        errors.put("message", "Ошибка авторизации во внешнем сервисе. Модерация временно недоступна.");
        return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
    }

    /**
     * Базовая обработка непредвиденных исключений времени выполнения.
     * Используется для предотвращения утечки технических данных в ответе API.
     *
     * @param e исключение RuntimeException
     * @return ResponseEntity со статусом 500 Internal Server Error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        log.error("Непредвиденная системная ошибка: {}", e.getMessage(), e);
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("error", "Internal Server Error");
        errors.put("message", "Внутренняя ошибка сервера.");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработка исключений, связанных с отсутствием запрашиваемых сущностей.
     * Используется для уведомления клиента о неверных идентификаторах в запросе.
     *
     * @param e исключение UserNotFoundException
     * @return ResponseEntity со статусом 404 Not Found
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException e) {
        log.error("Пользователь не найден): {}", e.getMessage());
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.NOT_FOUND.value());
        errors.put("error", "Not Found");
        errors.put("message", e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработка критических ошибок инфраструктуры базы данных.
     * Перехватывает системные сбои соединения, таймауты и ошибки драйвера.
     *
     * @param e исключение DataAccessException
     * @return ResponseEntity со статусом 503 Service Unavailable
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseConnException(DataAccessException e) {
        log.error("Ошибка базы данных: {}", e.getMessage(), e);
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        errors.put("error", "Service Unavailable");
        errors.put("message", "Сервис временно недоступен из-за проблем с базой данных.");
        return new ResponseEntity<>(errors, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Обработка специфических ошибок уровня доступа к данным.
     * Вызывается при нарушении бизнес-логики сохранения или обновления записей.
     *
     * @param e исключение DatabaseOperationException
     * @return ResponseEntity со статусом 503 Service Unavailable
     */
    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseOperationException(DatabaseOperationException e) {
        log.error("Ошибка базы данных: {}", e.getMessage(), e);
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        errors.put("error", "Service Unavailable");
        errors.put("message", "Сервис временно недоступен из-за проблем с базой данных. Пожалуйста, попробуйте позже.");
        return new ResponseEntity<>(errors, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Универсальный перехватчик всех типов проверяемых и непроверяемых исключений.
     * Гарантирует возврат стандартизированного ответа.
     *
     * @param e исключение Exception
     * @return ResponseEntity со статусом 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception e) {
        log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("error", "Internal Server Error");
        errors.put("message", "На сервере произошла непредвиденная ошибка.");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработка ошибок валидации полей входящего DTO.
     * Используется для уведомления клиента о некорректно заполненных полях запроса.
     *
     * @param e исключение MethodArgumentNotValidException
     * @return ResponseEntity со статусом 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Validation Error");
        Map<String, String> fieldErrors = new HashMap<>();
        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrorList) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
            log.error("Ошибка валидации поля {}: {}", error.getField(), error.getDefaultMessage());
        }
        errors.put("message", fieldErrors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка исключений, связанных с передачей недопустимых аргументов.
     * Используется для перехвата ошибок бизнес-логики на уровне входных данных.
     *
     * @param e исключение IllegalArgumentException
     * @return ResponseEntity со статусом 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Некорректный аргумент:", e.getMessage());
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Invalid Argument");
        errors.put("message", e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка нарушений целостности данных в базе данных.
     * Используется для уведомления о конфликтах, таких как дублирование уникальных значений.
     *
     * @param e исключение DataIntegrityViolationException
     * @return ResponseEntity со статусом 409 Conflict
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("Конфликт/дубликат данных ): {}", e.getMessage());
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.CONFLICT.value());
        errors.put("error", "Conflict");
        errors.put("message", "Запрос отклонен: данные конфликтуют с уже существующими записями.");
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    /**
     * Обработка ошибок чтения HTTP-сообщения.
     * Используется для уведомления клиента о синтаксических ошибках в JSON (например, пропущенные запятые или кавычки).
     *
     * @param e исключение HttpMessageNotReadableException
     * @return ResponseEntity со статусом 400 Bad Request
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException e) {
        log.warn("Ошибка чтения JSON в запросе: {}", e.getMessage());
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Bad Request");
        errors.put("message", "Некорректный формат JSON. Проверьте синтаксис");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
