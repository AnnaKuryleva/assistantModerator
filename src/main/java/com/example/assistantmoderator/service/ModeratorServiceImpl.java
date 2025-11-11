package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.ModeratorResponseDto;
import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;
import com.example.assistantmoderator.dto.MessageAnalysisDto.Message;
import com.example.assistantmoderator.dto.MessageAnalysisDto;
import com.example.assistantmoderator.dto.TokenResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

/**
 * Сервис модерации текста, использующий внешний API GigaChat.
 * <p>
 * Отвечает за **оркестрацию процесса модерации**: получает токен из {@link TokenService},
 * формирует запрос к API GigaChat, отправляет его и возвращает ответ.
 */
@Service("moderatorService")
public class ModeratorServiceImpl implements ModeratorService {

    private final WebClient webClient;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    private static final String MODERATION_URL = "https://gigachat.devices.sberbank.ru/api/v1/chat/completions";

    /**
     * Конструктор сервиса модерации.
     *
     * @param webClientBuilder Builder WebClient, настроенный через Spring IoC.
     * @param tokenService     Сервис для получения access токенов (используется для получения актуального токена).
     * @param objectMapper     ObjectMapper для парсинга JSON.
     */
    public ModeratorServiceImpl(WebClient.Builder webClientBuilder, TokenService tokenService, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(MODERATION_URL).build();
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    /**
     * Модерирует предоставленный пользовательский текст, отправляя его во внешний API GigaChat.
     * <p>
     * Включает получение токена, формирование HTTP-запроса, отправку данных и парсинг
     * вложенного JSON-ответа для получения финального результата модерации.
     *
     * @param userText Текст, введенный пользователем для проверки.
     * @return Объект ModeratorResultDto с результатами анализа (например, флаги модерации).
     * @throws RuntimeException Если происходит ошибка парсинга JSON из ответа API.
     */
    @Override
    public ModeratorUserTextResultDto moderateText(String userText) {
        TokenResponseDto tokenResponse = tokenService.getAccessToken();
        String accessToken = tokenResponse.getAccess_token();
        Message userMessage = new Message(userText);
        MessageAnalysisDto requestBody = new MessageAnalysisDto("GigaChat-2", Collections.singletonList(userMessage));

        ModeratorResponseDto apiResponse = webClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ModeratorResponseDto.class)
                .block();
        String contentString = apiResponse.getChoices().get(0).getMessage().getContent();

        try {
            return objectMapper.readValue(contentString, ModeratorUserTextResultDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при разборе строки из результата модерации : " + contentString, e);
        }
    }
}
