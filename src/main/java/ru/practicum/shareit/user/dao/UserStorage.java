package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserStorage {

    private Map<Long, User> userMap = new HashMap<>();

    public User get(Long id) {
        return userMap.get(id);
    }

    public void put(Long id, User user) {
        userMap.put(id, user);
    }

    public void remove(Long id) {
        userMap.remove(id);
    }

    public Collection<User> values() {
        return userMap.values();
    }

    public Collection<Long> keySet() {
        return userMap.keySet();
    }
}
