package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;
import com.example.assistantmoderator.exception.DatabaseOperationException;
import com.example.assistantmoderator.model.MessageAnalysis;
import com.example.assistantmoderator.repository.MessageAnalysisRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * Сервис для обновления существующей записи в БД с результатами модерации.
 * Устанавливает признак негатива и оценку на основе результата от GigaChat.
 * Сохраняет изменения через репозиторий.
 * Работает с уже сохранённой сущностью {@link MessageAnalysis}, полученной
 * от {@link com.example.assistantmoderator.service.RequestPersistenceServiceImpl}.
 */
@Service
public class ResponsePersistenceServiceImpl implements ResponsePersistenceService {
    private final MessageAnalysisRepository repository;

    public ResponsePersistenceServiceImpl(MessageAnalysisRepository repository) {
        this.repository = repository;
    }

    @Override
    public void persistResponce(MessageAnalysis entity, ModeratorUserTextResultDto textResultDto) {
        if (textResultDto == null) {
            throw new DatabaseOperationException("Невозможно сохранить результат: данные модерации отсутствуют", null);
        }
        try {
            entity.setModerationNegative(textResultDto.isNegative());
            entity.setModerationScore(textResultDto.getScore());
            repository.save(entity);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(
                    "Ошибка при обновлении записи модерации в БД для сущности с ID: " + entity.getId(), e);
        }
    }
}
