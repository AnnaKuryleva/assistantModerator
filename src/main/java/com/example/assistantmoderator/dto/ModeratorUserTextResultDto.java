package com.example.assistantmoderator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO, представляющий собой финальный структурированный результат модерации текста.
 * Этот объект получается после парсинга вложенного JSON из ответа GigaChat API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeratorUserTextResultDto {
    private boolean negative;
    private int score;
    private String context;
    private String tone;

}
