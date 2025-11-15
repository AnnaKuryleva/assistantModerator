package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.MessageInputDto;
import com.example.assistantmoderator.model.MessageAnalysis;

public interface RequestPersistenceService {
    MessageAnalysis persistRequest(MessageInputDto inputDto);
}
