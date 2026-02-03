package com.example.assistantmoderator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    private Long id;

    @Column(name = "is_blocked")
    private boolean isBlocked = false;

    @Formula("(SELECT COUNT(*) FROM message_analysis m WHERE m.user_id = id AND m.moderation_negative = " +
            "true AND m.moderation_score >= 9)")
    private int negativeCount;
}

