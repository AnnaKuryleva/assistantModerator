package com.example.assistantmoderator.service;

import com.example.assistantmoderator.model.User;

import java.util.List;

public interface UserService {
    List<User> getBlockedUsers();
}
