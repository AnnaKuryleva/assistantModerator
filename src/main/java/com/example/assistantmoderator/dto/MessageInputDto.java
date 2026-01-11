package com.example.assistantmoderator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для приема входных данных от пользователя (текст и ID пользователя).
 */
@Data
public class MessageInputDto {

    @NotNull(message = "ID пользователя не может быть пустым")
    private Long userId;

    @NotBlank(message = "Текст сообщения не может быть пустым")
    private String userText;
}
