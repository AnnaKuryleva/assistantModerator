package com.example.assistantmoderator.controller;

import com.example.assistantmoderator.dto.MessageInputDto;
import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;
import com.example.assistantmoderator.service.ModeratorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для обработки запросов модерации текста.
 * Принимает входящие сообщения от пользователей и передаёт их в {@link ModeratorService}
 * для проверки на соответствие правилам.
 */
@Slf4j
@RestController
@RequestMapping("/api/moderation")
public class ModeratorController {
    private final ModeratorService moderatorService;

    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    /**
     * @param inputDto объект с данными пользователя и текстом (не может быть null)
     * @return результат модерации в виде {@link ModeratorUserTextResultDto}
     */
    @PostMapping("/checkText")
    public ModeratorUserTextResultDto checkText(@Valid @RequestBody MessageInputDto inputDto) {
        log.info("Запрос модерации для текста от пользователя {}: {}", inputDto.getUserId(), inputDto.getUserText());
        ModeratorUserTextResultDto resultTextDto = moderatorService.moderateText(inputDto);
        log.info("Модерация успешно завершена");
        return resultTextDto;
    }
}
