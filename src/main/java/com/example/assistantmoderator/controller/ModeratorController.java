package com.example.assistantmoderator.controller;

import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;
import com.example.assistantmoderator.service.ModeratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * **REST Controller:** Обрабатывает входящие HTTP-запросы, связанные с функционалом модерации текста.
 * Предоставляет публичный API-эндпоинт для проверки пользовательских сообщений.
 */
@Slf4j
@RestController
@RequestMapping("/api/moderation")
public class ModeratorController {
    private final ModeratorService moderatorService;

    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    /**
     * **API Endpoint:** Принимает пользовательский текст и возвращает результат его модерации.
     * Обрабатывает POST-запросы на URL "/api/moderation/checkText".
     * Тело запроса должно содержать сырую строку текста (Content-Type: application/json или text/plain).
     *
     * @param userText Текст, введенный пользователем для модерации.
     * @return Объект {@link ModeratorUserTextResultDto}, содержащий структурированные результаты анализа текста.
     */
    @PostMapping("/checkText")
    public ModeratorUserTextResultDto checkText(@RequestBody String userText) {
        log.info("Запрос модерации для текста: {}", userText);
        ModeratorUserTextResultDto resultTextDto = moderatorService.moderateText(userText);
        log.info("Модерация успешно завершена. Негативный флаг: {}, Оценка: {}",
                resultTextDto.isNegative(), resultTextDto.getScore());
        return resultTextDto;
    }
}
