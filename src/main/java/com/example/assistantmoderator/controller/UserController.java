package com.example.assistantmoderator.controller;

import com.example.assistantmoderator.model.User;
import com.example.assistantmoderator.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * REST-контроллер для административного управления пользователями.
     *  На данном этапе обеспечивает доступ к операциям:
     * Получение списка всех заблокированных пользователей системы.
     */
    @GetMapping("/blocked")
    @Operation(summary = "Список заблокированных пользователей",
            description = "Возвращает список всех пользователей, которые в данный момент заблокированы")
    public ResponseEntity<List<User>> getBlockedUsers() {
        List<User> blockedUsers = userService.getBlockedUsers();
        log.info("Запрошен список заблокированных пользователей. Найдено: {}", blockedUsers.size());
        return ResponseEntity.ok(blockedUsers);
    }
}
