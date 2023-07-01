package ru.practicum.shareit.User;

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

    @Test
    void createUserTest() {
        UserDto userDto = UserDto.builder().name("Алеша").email("alesha@.yandex.ru").build();
        User user = UserMapper.toUser(userDto);
        Mockito.when(userService.createUser(user)).thenReturn(user);

        User newUser = UserMapper.toUser(userController.createUser(userDto));

        Assertions.assertEquals(user, newUser);
    }

    @Test
    void updateUserTest() {
        UserDto userDto = UserDto.builder().id(1L).name("Алеша").email("alesha@.yandex.ru").build();
        User user = UserMapper.toUser(userDto);
        user.setName("Григорий");
        Mockito.when(userService.updateUser(1L, user)).thenReturn(user);
        userDto.setName("Григорий");
        User newUser = UserMapper.toUser(userController.updateUser(1L, userDto));

        Assertions.assertEquals(user, newUser);
    }

    @Test
    void findAllUsersTest() {
        List<User> listUser = List.of(User.builder().id(1L).name("Алеша").email("alesha@.yandex.ru").build());

        Mockito.when(userService.findAllUsers()).thenReturn(listUser);

        List<User> newListUser = userController.findAllUsers().stream().map(UserMapper::toUser).collect(Collectors.toList());

        Assertions.assertEquals(listUser, newListUser);
    }

    @Test
    void findUserTest() {
        UserDto userDto = UserDto.builder().id(1L).name("Алеша").email("alesha@.yandex.ru").build();
        User user = UserMapper.toUser(userDto);

        Mockito.when(userService.findUserById(1L)).thenReturn(user);

        User newUser = UserMapper.toUser(userController.findUser(1L));

        Assertions.assertEquals(user, newUser);
    }

    @Test
    void deleteUserTest() {
        long userId = 1L;
        userController.deleteUser(1L);
        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
    }
}
