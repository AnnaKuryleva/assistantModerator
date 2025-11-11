package com.example.assistantmoderator.model;

import lombok.Data;

import java.time.LocalDateTime;

//@Entity
//@Table(name = "message_user")
@Data
public class MessageUser {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userText;
    private LocalDateTime timestamp;
    private boolean moderationNegative;
    private int moderationScore;
}

