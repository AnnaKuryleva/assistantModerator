package com.example.assistantmoderator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Содержит токен доступа и время его истечения.
 * Проверяет, истёк ли срок действия токена.
 * Учитывает запас в 60 секунд до истечения срока.
 * @return true, если токен просрочен или скоро истечёт
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private String access_token;
    private Long expires_at;

    public boolean isExpired() {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds >= (expires_at - 60);
    }
}
