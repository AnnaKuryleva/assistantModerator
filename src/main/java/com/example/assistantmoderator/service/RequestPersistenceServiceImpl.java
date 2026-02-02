package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.MessageInputDto;
import com.example.assistantmoderator.exception.DatabaseOperationException;
import com.example.assistantmoderator.mapper.MessageAnalysisMapper;
import com.example.assistantmoderator.model.MessageAnalysis;
import com.example.assistantmoderator.repository.MessageAnalysisRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * Сервис для сохранения входящего сообщения в базу данных.
 * Преобразует DTO в сущность с помощью {@link MessageAnalysisMapper},
 * устанавливает время поступления сообщения и сохраняет через {@link MessageAnalysisRepository}.
 */
@Service
public class RequestPersistenceServiceImpl implements RequestPersistenceService {

    private final MessageAnalysisRepository repository;
    private final MessageAnalysisMapper messageAnalysisMapper;

    public RequestPersistenceServiceImpl(MessageAnalysisRepository repository, MessageAnalysisMapper messageAnalysisMapper) {
        this.repository = repository;
        this.messageAnalysisMapper = messageAnalysisMapper;
    }

    public MessageAnalysis persistRequest(MessageInputDto inputDto) {
        MessageAnalysis entity = messageAnalysisMapper.toEntity(inputDto);
        entity.setMessageArrivalTime(java.time.LocalDateTime.now());
        try {
            return repository.save(entity);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Ошибка базы данных при сохранении текста: " + inputDto.getUserText(), e);
        }
    }
}
