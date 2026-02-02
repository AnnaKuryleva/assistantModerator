package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.*;
import com.example.assistantmoderator.exception.ParsingResponseGigaChatException;
import com.example.assistantmoderator.http.GigaChatHttpClient;
import com.example.assistantmoderator.model.MessageAnalysis;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit-tests ModeratorServiceImpl")
public class ModeratorServiceImplTest {

    @Mock
    private TokenService tokenService;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private RequestPersistenceServiceImpl requestPersistenceService;

    @Mock
    private GigaChatHttpClient gigaChatClient;

    @InjectMocks
    private ModeratorServiceImpl moderatorService;

    @Test
    @DisplayName("Метод должен выбрасывать ParsingResponseGigaChatException при невозможности распарсить JSON")
    void whenResponseFromGigaChatCannotBeProcessedWillReceiveAnParsingResponseGigaChatException() {
        MessageInputDto input = new MessageInputDto();
        input.setUserId(1L);
        input.setUserText("Текст для модерации");
        MessageAnalysis entity = new MessageAnalysis();
        entity.setUserText("Текст для модерации");

        when(requestPersistenceService.persistRequest(any())).thenReturn(entity);
        when(tokenService.getAccessToken()).thenReturn(new TokenResponseDto("mockToken", 9999999999L));

        String badContent = "```json { \"negative\": true, \"score\": 5 ";
        ModeratorResponseDto.InnerMessage innerMessage =
                new ModeratorResponseDto.InnerMessage(badContent, "assistant");
        ModeratorResponseDto.Choice choice = new ModeratorResponseDto.Choice();
        choice.setMessage(innerMessage);
        choice.setIndex(0);
        choice.setFinish_reason("stop");
        ModeratorResponseDto mockApiResponse = new ModeratorResponseDto();
        mockApiResponse.setChoices(Collections.singletonList(choice));

        when(gigaChatClient.sendRequest(any(), any())).thenReturn(mockApiResponse);
        assertThrows(ParsingResponseGigaChatException.class, () -> moderatorService.moderateText(input));
    }
}





