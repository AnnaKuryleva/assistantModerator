package com.example.assistantmoderator.http;

import com.example.assistantmoderator.dto.GigaChatRequestDto;
import com.example.assistantmoderator.dto.ModeratorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * HTTP-клиент для взаимодействия с GigaChat API.
 * Базовый URL: {@value #MODERATION_URL}.
 */
@Component
public class GigaChatHttpClient {
    private final WebClient webClient;
    private static final String MODERATION_URL = "https://gigachat.devices.sberbank.ru/api/v1/chat/completions";

    public GigaChatHttpClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(MODERATION_URL)
                .build();
    }

    /**
     * Отправляет запрос в GigaChat API для модерации текста.
     *
     * @param request      тело запроса с данными для модерации ({@link GigaChatRequestDto})
     * @param accessToken  токен доступа (Bearer-токен)
     * @return ответ от GigaChat в виде {@link ModeratorResponseDto}
     */
    public ModeratorResponseDto sendRequest(GigaChatRequestDto request, String accessToken) {
        return webClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ModeratorResponseDto.class)
                .block();
    }
}
