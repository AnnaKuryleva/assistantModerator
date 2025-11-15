package com.example.assistantmoderator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * JPA-сущность для хранения результатов модерации текста.
 * Соответствует таблице {@code message_analysis} в базе данных.
 * Планируется использовать для сохранения истории проверок сообщений пользователей.
 */
@Entity
@Table(name = "message_analysis")
@Data
public class MessageAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String userText;
    private LocalDateTime messageArrivalTime;
    private boolean moderationNegative;
    private int moderationScore;
}
