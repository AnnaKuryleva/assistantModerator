package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.GigaChatRequestDto;
import com.example.assistantmoderator.dto.GigaChatRequestDto.Message;
import com.example.assistantmoderator.dto.MessageInputDto;
import com.example.assistantmoderator.dto.ModeratorResponseDto;
import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;
import com.example.assistantmoderator.dto.TokenResponseDto;
import com.example.assistantmoderator.exception.ParsingResponseGigaChatException;
import com.example.assistantmoderator.http.GigaChatHttpClient;
import com.example.assistantmoderator.model.MessageAnalysis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Сервис модерации текста через GigaChat API.
 * Основная логика:
 * Принимает входящее сообщение.
 * Передаёт его на сохранение в {@link RequestPersistenceServiceImpl}
 * Получает токен доступа через {@link TokenService}
 * Формирует запрос и отправляет его в GigaChat с помощью {@link GigaChatHttpClient}
 * Парсит ответ.
 * Отдаёт результат модерации в {@link ResponcePersistenceServiceImpl} для обновления записи.
 * Возвращает результат клиенту
 */
@Service("moderatorService")
public class ModeratorServiceImpl implements ModeratorService {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final RequestPersistenceServiceImpl requestPersistenceService;
    private final ResponcePersistenceServiceImpl responcePersistence;
    private final GigaChatHttpClient gigaChatClient;

    public ModeratorServiceImpl(TokenService tokenService, ObjectMapper objectMapper,
                                RequestPersistenceServiceImpl requestPersistence, ResponcePersistenceServiceImpl responcePersistence,
                                GigaChatHttpClient gigaChatClient) {
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        this.requestPersistenceService = requestPersistence;
        this.responcePersistence = responcePersistence;
        this.gigaChatClient = gigaChatClient;
    }

    @Override
    public ModeratorUserTextResultDto moderateText(MessageInputDto inputDto) {
        MessageAnalysis entity = requestPersistenceService.persistRequest(inputDto);
        String userText = entity.getUserText();
        TokenResponseDto tokenResponse = tokenService.getAccessToken();
        String accessToken = tokenResponse.getAccess_token();
        Message userMessage = new Message(userText);
        GigaChatRequestDto requestBody = new GigaChatRequestDto("GigaChat-2", Collections.singletonList(userMessage));
        ModeratorResponseDto apiResponse = gigaChatClient.sendRequest(requestBody, accessToken);
        String contentString = apiResponse.getChoices().get(0).getMessage().getContent();
        String cleanJsonString = contentString.replaceAll("^```json\\s*|\\s*```$", "").trim();
        ModeratorUserTextResultDto textResultDto;
        try {
            textResultDto = objectMapper.readValue(cleanJsonString, ModeratorUserTextResultDto.class);
        } catch (JsonProcessingException e) {
            throw new ParsingResponseGigaChatException("Ошибка парсинга JSON. Контент: " + cleanJsonString, e);
        }
        responcePersistence.persistResponce(entity, textResultDto);
        return textResultDto;
    }
}
