package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailDuplicateException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private Long nextId = 1L;

    private Map<Long, User> map = new HashMap<>();

    @Override
    public User createUser(User user) {
        checkEmail(nextId, user);
        user.setId(nextId);
        map.put(nextId++, user);
        return user;
    }

    @Override
    public User findUser(Long id) {
        return map.get(id);
    }

    @Override
    public User updateUser(Long userId, User user) {
        if (!map.get(userId).getEmail().equals(user.getEmail())) {
            checkEmail(userId, user);
        }
        checkUserName(userId, user);
        checkUserEmail(userId, user);

        user.setId(userId);
        map.put(userId, user);
        return map.get(userId);
    }

    @Override
    public void deleteUser(Long id) {
        map.remove(id);
    }

    @Override
    public List<User> findAllUsers() {
        return map.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserId() {
        return map.keySet().stream().collect(Collectors.toList());
    }

    private void checkEmail(Long id, User user) {
        boolean answer = map.values()
                .stream()
                .filter(a -> a.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList())
                .isEmpty();
        if (answer != true) {
            throw new EmailDuplicateException("Данный email уже существует");
        }
    }

    private void checkUserName(Long id, User user) {
        if (user.getName() == null) {
            user.setName(map.get(id).getName());
        } else {
            map.get(id).setName(user.getName());
        }
    }

    private void checkUserEmail(Long id, User user) {
        if (user.getEmail() == null) {
            user.setEmail(map.get(id).getEmail());
        } else {
            map.get(id).setEmail(user.getEmail());
        }
    }
}
