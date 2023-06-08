package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserStorage {

    private Long nextId = 1L;

    private Map<Long, User> userMap = new HashMap<>();

    public User get(Long id) {
        return userMap.get(id);
    }

    public void put(User user) {
        user.setId(nextId);
        userMap.put(nextId++, user);
    }

    public void remove(Long id) {
        userMap.remove(id);
    }

    public Collection<User> values() {
        return List.copyOf(userMap.values());
    }

    public Collection<Long> keySet() {
        return userMap.keySet();
    }

    public Boolean existById(Long id) {
        Boolean answer = userMap.keySet().stream().collect(Collectors.toList()).contains(id);
        return answer;
    }

}
