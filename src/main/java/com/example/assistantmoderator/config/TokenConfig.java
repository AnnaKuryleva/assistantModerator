package com.example.assistantmoderator.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * **Конфигурационный компонент (Configuration Component):** Содержит набор статических,
 * неизменяемых параметров (констант), необходимых для выполнения OAuth2 запроса
 * к сервису авторизации Сбербанка.
 */
@Data
@AllArgsConstructor
@Component
public class TokenConfig {
    private final String contentType = "application/x-www-form-urlencoded";
    private final String accept = "application/json";
    private final String rqUid = "6f0b1291-c7f3-43c6-bb2e-9f3efb2dc98e";
    private final String authorizationBasic = "Basic MDE5YTYyODEtM2U3NS03MzYyLTllYTctOGJmMzcwYzgzYTZkOjI4ZTkyZTUwLWMzYTAtNGI3Yy04OTgxLTE1ODg3NmJiZGIzOQ==";
    private final String scope = "GIGACHAT_API_PERS";
}


