package com.example.assistantmoderator.service;

import com.example.assistantmoderator.model.User;
import com.example.assistantmoderator.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** * Сервис для работы с данными пользователей системы модерации.
     * * На текущем этапе обеспечивает:
     * * Получение актуального списка пользователей, которые были автоматически
     * * заблокированы системой за превышение лимита негативных сообщений.
     * * Взаимодействие с {@link UserRepository} для извлечения данных о статусе блокировки.
     * */
    @Override
    public List<User> getBlockedUsers() {
        return userRepository.findAllByIsBlockedTrue();
    }
}
