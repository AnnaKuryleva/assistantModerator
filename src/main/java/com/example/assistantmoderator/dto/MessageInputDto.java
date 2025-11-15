package com.example.assistantmoderator.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для приема входных данных от пользователя (текст и ID пользователя).
 */
@Data
public class MessageInputDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID пользователя не может быть пустым")
    private Long userId;

    @NotBlank(message = "Текст сообщения не может быть пустым")
    private String userText;
}
