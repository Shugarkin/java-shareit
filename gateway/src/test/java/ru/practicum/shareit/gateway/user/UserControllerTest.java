package ru.practicum.shareit.gateway.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.client.UserClient;
import ru.practicum.controller.UserController;
import ru.practicum.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);


    private UserDto userDto = UserDto.builder().name("Алеша").email("alesha@.yandex.ru").build();

    private long userId = 1L;

    @Test
    void createUser() {

        Mockito.when(userClient.createUser(userDto)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> user = userController.createUser(userDto);

        Assertions.assertEquals(user, objectResponseEntity);
    }

    @Test
    void updateUser() {
        Mockito.when(userClient.updateUser(userId, userDto)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> objectResponseEntity1 = userController.updateUser(userId, userDto);

        Assertions.assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    void findAllUsers() {

        Mockito.when(userClient.findAllUsers()).thenReturn(objectResponseEntity);

        ResponseEntity<Object> allUsers = userController.findAllUsers();

        Assertions.assertEquals(objectResponseEntity, allUsers);
    }

    @Test
    void findUser() {

        Mockito.when(userClient.findUser(userId)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> user = userController.findUser(userId);

        Assertions.assertEquals(objectResponseEntity, user);
    }

    @Test
    void deleteUser() {

        userController.deleteUser(userId);
        Mockito.verify(userClient, Mockito.times(1)).deleteUser(userId);
    }
}

