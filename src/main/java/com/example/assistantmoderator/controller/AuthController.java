package com.example.assistantmoderator.controller;

import com.example.assistantmoderator.dto.TokenResponseDto;
import com.example.assistantmoderator.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * **Контроллер для отладки и тестирования:**
 * **Будет удалён**
 * Предоставляет эндпоинт для ручной проверки работоспособности сервиса получения токенов {@link TokenService}.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Возвращает актуальный Access Token от API Сбербанка.
     * Используется для отладки.
     *
     * @return Объект {@link TokenResponseDto}, содержащий токен и срок его действия.
     */
    @GetMapping("/token")
    @Operation(
            summary = "Получить текущий Access Token",
            description = "ВНИМАНИЕ: Тестовый эндпоинт и будет удален. Возвращает актуальный токен GigaChat для проверки авторизации" +
                    " и ручной отладки запросов."
    )
    public TokenResponseDto getToken() {
        log.info("Запрос токена в AuthController.");
        TokenResponseDto token = tokenService.getAccessToken();
        log.info("Токен успешно получен и возвращен.");
        return token;
    }
}
