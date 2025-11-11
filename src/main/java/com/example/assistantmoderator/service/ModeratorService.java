package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;

public interface ModeratorService {
    ModeratorUserTextResultDto moderateText(String userText);
}
