package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User findUser(Long id);

    User updateUser(Long userId, User user);

    void deleteUser(Long id);

    List<User> findAllUsers();

    List<Long> getUserId();
}
