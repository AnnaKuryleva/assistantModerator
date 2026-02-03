package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;
import com.example.assistantmoderator.exception.DatabaseOperationException;
import com.example.assistantmoderator.model.MessageAnalysis;
import com.example.assistantmoderator.repository.UserRepository;
import com.example.assistantmoderator.model.User;
import com.example.assistantmoderator.repository.MessageAnalysisRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * Сервис для обновления существующей записи в БД с результатами модерации.
 * Устанавливает признак негатива и оценку на основе результата от GigaChat.
 * Сохраняет изменения через репозиторий.
 * Работает с уже сохранённой сущностью {@link MessageAnalysis}, полученной
 * от {@link com.example.assistantmoderator.service.RequestPersistenceServiceImpl}.
 */
@Slf4j
@Service
public class ResponsePersistenceServiceImpl implements ResponsePersistenceService {
    private final MessageAnalysisRepository repository;
    private final UserRepository userRepository;

    public ResponsePersistenceServiceImpl(MessageAnalysisRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
            User user = entity.getUser();
            if (user.getNegativeCount() >= 3) {
                user.setBlocked(true);
                userRepository.save(user);
                log.warn("Пользователь с ID {} заблокирован", user.getId());
            }
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(
                    "Ошибка при обновлении записи модерации в БД для сущности с ID: " + entity.getId(), e);
        }
    }
}
