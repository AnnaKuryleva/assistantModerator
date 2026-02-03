package com.example.assistantmoderator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA-сущность для хранения результатов модерации текста.
 * Соответствует таблице {@code message_analysis} в базе данных.
 * Планируется использовать для сохранения истории проверок сообщений пользователей.
 */
@Entity
@Table(name = "message_analysis")
@Getter
@Setter
public class MessageAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "user_text", columnDefinition = "TEXT")
    private String userText;
    @Column(name = "message_arrival_time")
    private LocalDateTime messageArrivalTime;
    @Column(name = "moderation_negative")
    private boolean moderationNegative;
    @Column(name = "moderation_score")
    private int moderationScore;
}
