package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;
import com.example.assistantmoderator.model.MessageAnalysis;

public interface ResponcePersistenceService {
    void persistResponce(MessageAnalysis entity, ModeratorUserTextResultDto textResultDto);
}
