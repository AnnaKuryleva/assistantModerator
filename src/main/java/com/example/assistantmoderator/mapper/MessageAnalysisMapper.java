package com.example.assistantmoderator.mapper;

import com.example.assistantmoderator.dto.MessageInputDto;
import com.example.assistantmoderator.model.MessageAnalysis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования данных из входного DTO ({@link MessageInputDto})
 * в сущность базы данных ({@link MessageAnalysis}).
 * Используется фреймворк MapStruct для автоматической генерации реализации.
 */
@Mapper(componentModel = "spring")
public interface MessageAnalysisMapper {

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "userText", source = "userText")
    MessageAnalysis toEntity(MessageInputDto inputDto);
}