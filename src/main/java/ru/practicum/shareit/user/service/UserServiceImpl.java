package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    @Override
    public User updateUser(Long userId, User user) {
        User userOld = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        String email = user.getEmail();
        String name = user.getName();

        if (name != null && !name.isBlank()) {
            userOld.setName(name);
        }
        if (email != null && !email.isBlank()) {
            userOld.setEmail(email);
        }
        return userRepository.save(userOld);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
