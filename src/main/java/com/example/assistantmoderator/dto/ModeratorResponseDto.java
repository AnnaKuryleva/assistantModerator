package com.example.assistantmoderator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * **DTO** Представляет собой полный объект ответа от внешнего API GigaChat
 * после запроса модерации или генерации текста.
 * Этот класс используется для автоматического маппинга JSON-ответа в Java-объект с помощью Jackson.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeratorResponseDto {
    private List<Choice> choices;
    private long created;
    private String model;
    private String object;
    private Usage usage;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private InnerMessage message;
        private int index;
        private String finish_reason;
    }

    /**
     * Вложенный класс DTO, содержащий само сообщение ответа от ассистента.
     * Поле 'content' содержит вложенный JSON с результатами модерации.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InnerMessage {
        private String content;
        private String role;
    }

    /**
     * Вложенный класс DTO, содержащий информацию об использовании токенов в запросе.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
        private int precached_prompt_tokens;
    }
}
