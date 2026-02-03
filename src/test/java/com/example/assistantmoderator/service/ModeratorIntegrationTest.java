package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.MessageInputDto;
import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;
import com.example.assistantmoderator.model.MessageAnalysis;
import com.example.assistantmoderator.repository.MessageAnalysisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ModeratorIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ModeratorServiceImpl moderatorService;
    @Autowired
    private MessageAnalysisRepository messageAnalysisRepository;

    @Test
    @DisplayName("Полный цикл модерации текста")
    void WhenTextIsReceivedRecordShouldBeSavedInDatabaseAndResultWillBePositive() {
        String expectedText = "Ты очень хороший человек!";
        MessageInputDto input = new MessageInputDto();
        input.setUserId(1L);
        input.setUserText(expectedText);

        ModeratorUserTextResultDto result = moderatorService.moderateText(input);

        assertNotNull(result, "Результат не должен быть null");
        assertFalse(result.isNegative(), "Текст не должен быть помечен как негативный");

        Optional<MessageAnalysis> savedMessageOpt = messageAnalysisRepository.findByUserText(expectedText);

        assertTrue(savedMessageOpt.isPresent(), "Запись с таким текстом не найдена в БД");
        assertEquals(1L, savedMessageOpt.get().getUser().getId(), "ID пользователя не совпадает");
    }
}
