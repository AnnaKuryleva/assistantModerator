package com.example.assistantmoderator.service;

import com.example.assistantmoderator.dto.MessageInputDto;
import com.example.assistantmoderator.dto.ModeratorUserTextResultDto;

public interface ModeratorService {
     ModeratorUserTextResultDto moderateText(MessageInputDto inputDto);

}
