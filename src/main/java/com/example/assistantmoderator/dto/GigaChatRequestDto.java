package com.example.assistantmoderator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * **DTO** Представляет собой тело исходящего POST-запроса
 * к API GigaChat для модерации или генерации текста.
 * Содержит модель GigaChat_2 для использования и список сообщений для обработки.
 */
@Data
@NoArgsConstructor
public class GigaChatRequestDto {
    private String model;
    private List<Message> messages;

    public GigaChatRequestDto(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    /**
     * Вложенный DTO, представляющий структуру отдельного сообщения (промпта) внутри запроса к GigaChat API.
     */
    @Getter
    @AllArgsConstructor
    public static class Message {
        private final String role = "user";
        private final String content;
        private final String userText;

        private static final String PROMPT_TEMPLATE =
//                "Проверьте наличие негативных высказываний в следующем сообщении: \"%s\". Ваш ответ должен" +
//                        " содержать только чистый JSON-объект, без какого-либо дополнительного текста, комментариев или " +
//                        "форматирования Markdown(например, без ```json или ```). Структура JSON должна быть следующей: " +
//                        "{\"negative\": boolean, \"score\": number, \"context\": string, \"tone\": string}";
        "Проанализируй текст на наличие негатива: \"%s\". " +
                "Твой ответ должен быть только в формате JSON, без лишних слов и Markdown-разметки. " +
                "Правила для поля 'score': " +
                "1. Если текст позитивный или нейтральный, score всегда равен 0. " +
                "2. Если текст агрессию, оскорбления, мат, токсичность,  то оцени степень негатива от 1 до 10, " +
                "где 10 — это крайне агрессивное сообщение с угрозой для жизни " +
                "Структура JSON: " +
                "{\"negative\": boolean, \"score\": number, \"context\": string, \"tone\": string}";

        public Message(String userText) {
            this.content = String.format(PROMPT_TEMPLATE, userText);
            this.userText = userText;
        }
    }
}
