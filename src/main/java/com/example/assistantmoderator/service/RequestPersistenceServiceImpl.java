package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.MessageInputDto;
import com.example.assistantmoderator.exception.DatabaseOperationException;
import com.example.assistantmoderator.mapper.MessageAnalysisMapper;
import com.example.assistantmoderator.model.MessageAnalysis;
import com.example.assistantmoderator.model.User;
import com.example.assistantmoderator.repository.UserRepository;
import com.example.assistantmoderator.repository.MessageAnalysisRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для сохранения входящего сообщения в базу данных.
 * Преобразует DTO в сущность с помощью {@link MessageAnalysisMapper},
 * устанавливает время поступления сообщения и сохраняет через {@link MessageAnalysisRepository}.
 */
@Service
public class RequestPersistenceServiceImpl implements RequestPersistenceService {

    private final MessageAnalysisRepository repository;
    private final MessageAnalysisMapper messageAnalysisMapper;
    private final UserRepository userRepository;

    public RequestPersistenceServiceImpl(MessageAnalysisRepository repository, MessageAnalysisMapper messageAnalysisMapper, UserRepository userRepository) {
        this.repository = repository;
        this.messageAnalysisMapper = messageAnalysisMapper;
        this.userRepository = userRepository;
    }

    public MessageAnalysis persistRequest(MessageInputDto inputDto) {
        MessageAnalysis entity = messageAnalysisMapper.toEntity(inputDto);
        Long id = inputDto.getUserId();
        Optional<User> userOptional = userRepository.findById(id);
        User user;
        if(userOptional.isPresent()){
            user = userOptional.get();
        } else {
            user = new User();
            user.setId(id);
            userRepository.save(user);
        }
        entity.setUser(user);
        entity.setMessageArrivalTime(java.time.LocalDateTime.now());
        try {
            return repository.save(entity);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Ошибка базы данных при сохранении текста: " + inputDto.getUserText(), e);
        }
    }
}
