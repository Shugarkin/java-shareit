package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    private UserDto userDto = UserDto.builder().name("Алеша").email("alesha@.yandex.ru").build();

    private long userId = 1L;

    @Test
    void createUser() {
        User user = UserMapper.toUser(userDto);
        Mockito.when(userService.createUser(user)).thenReturn(user);

        User newUser = UserMapper.toUser(userController.createUser(userDto));

        Assertions.assertEquals(user, newUser);
    }

    @Test
    void updateUser() {
        User user = UserMapper.toUser(userDto);
        user.setName("Григорий");
        Mockito.when(userService.updateUser(userId, user)).thenReturn(user);
        userDto.setName("Григорий");
        User newUser = UserMapper.toUser(userController.updateUser(userId, userDto));

        Assertions.assertEquals(user, newUser);
    }

    @Test
    void findAllUsers() {
        User user = UserMapper.toUser(userDto);
        List<User> listUser = List.of(user);

        Mockito.when(userService.findAllUsers()).thenReturn(listUser);

        List<User> newListUser = userController.findAllUsers().stream().map(UserMapper::toUser).collect(Collectors.toList());

        Assertions.assertEquals(listUser, newListUser);
    }

    @Test
    void findUser() {
        User user = UserMapper.toUser(userDto);

        Mockito.when(userService.findUserById(userId)).thenReturn(user);

        User newUser = UserMapper.toUser(userController.findUser(userId));

        Assertions.assertEquals(user, newUser);
    }

    @Test
    void deleteUser() {

        userController.deleteUser(userId);
        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
    }
}
