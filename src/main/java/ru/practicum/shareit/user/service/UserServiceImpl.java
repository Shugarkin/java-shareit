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

    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        checkEmail(user);
        userStorage.put(user);
        return user;
    }

    @Override
    public User findUser(Long id) {
        return userStorage.get(id);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User userOld = userStorage.get(userId);
        String email = user.getEmail();
        String name = user.getName();

        if (!userOld.getEmail().equals(email)) {
            checkEmail(user);
        }
        if (name != null && !name.isBlank()) {
            userOld.setName(name);
        }
        if (email != null && !email.isBlank()) {
            userOld.setEmail(email);
        }
        return userOld;
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

    private void checkEmail(User user) {
        boolean answer = userStorage.values()
                .stream()
                .filter(a -> a.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList())
                .isEmpty();
        if (answer != true) {
            throw new EmailDuplicateException("Данный email уже существует");
        }
    }
}
