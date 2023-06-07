package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailDuplicateException;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private Long nextId = 1L;

    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        checkEmail(nextId, user);
        user.setId(nextId);
        userStorage.put(nextId++, user);
        return user;
    }

    @Override
    public User findUser(Long id) {
        return userStorage.get(id);
    }

    @Override
    public User updateUser(Long userId, User user) {
        if (!userStorage.get(userId).getEmail().equals(user.getEmail())) {
            checkEmail(userId, user);
        }
        checkUserName(userId, user);
        checkUserEmail(userId, user);

        user.setId(userId);
        userStorage.put(userId, user);
        return userStorage.get(userId);
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.remove(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userStorage.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserId() {
        return userStorage.keySet().stream().collect(Collectors.toList());
    }

    private void checkEmail(Long id, User user) {
        boolean answer = userStorage.values()
                .stream()
                .filter(a -> a.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList())
                .isEmpty();
        if (answer != true) {
            throw new EmailDuplicateException("Данный email уже существует");
        }
    }

    private void checkUserName(Long id, User user) {
        if (user.getName() == null || user.getName().isBlank() ) {
            user.setName(userStorage.get(id).getName());
        } else {
            userStorage.get(id).setName(user.getName());
        }
    }

    private void checkUserEmail(Long id, User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(userStorage.get(id).getEmail());
        } else {
            userStorage.get(id).setEmail(user.getEmail());
        }
    }
}
