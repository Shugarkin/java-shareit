package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EmailDuplicateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser() {
        User user = User.builder().build();

        Mockito.when(userRepository.save(user)).thenReturn(user);

        User newUser = userService.createUser(user);

        Assertions.assertEquals(user, newUser);

    }


    @Test
    void createUserDuplicate() {
        User user = User.builder().email("asd@.mail.ru").build();
        User user1 = User.builder().email("asd@.mail.ru").build();

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.save(user1)).thenThrow(EmailDuplicateException.class);

        Assertions.assertThrows(EmailDuplicateException.class, () -> userService.createUser(user1));
    }

    @Test
    void findUserById() {
        User user = User.builder().build();
        long userId = 0L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User newUser = userService.findUserById(userId);

        Assertions.assertEquals(user, newUser);
    }

    @Test
    void findUserByIdThrown() {
        long userId = 0L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.findUserById(userId));
    }

    @Test
    void updateUser() {
        User userOld = User.builder().id(1L).name("Алеша").email("alesha@.yandex.ru").build();
        User userNew = User.builder().id(1L).name("Алешша").email("aalesha@.yandex.ru").build();
        long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userOld));

        User updateUser = userService.updateUser(userId, userNew);

        Assertions.assertEquals(userOld, updateUser);
        Assertions.assertEquals(userNew, updateUser);
    }

    @Test
    void updateUserThrown() {
        User user = User.builder().id(1L).name("Алеша").email("alesha@.yandex.ru").build();
        long userId = 0L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, user));

        Mockito.verify(userRepository, Mockito.never()).save(user);
    }


    @Test
    void findAllUsers() {
        List<User> list = List.of(User.builder().id(1L).name("Алеша").email("alesha@.yandex.ru").build());

        Mockito.when(userRepository.findAll()).thenReturn(list);

        List<User> newList = userService.findAllUsers();

        Assertions.assertEquals(list, newList);
    }

    @Test
    void deleteUser() {
        long userId = 1L;
        userService.deleteUser(userId);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }
}
