package ru.practicum.shareit.gateway.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.client.UserClient;
import ru.practicum.controller.UserController;
import ru.practicum.dto.ItemRequestDtoReceived;
import ru.practicum.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    private long userId = 1L;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    private UserDto user = UserDto.builder().email("asd@mail.ru").name("asd").build();


    @SneakyThrows
    @Test
    void createUser() {
        when(userClient.createUser(user)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/users", user)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void createUserNotValid() {
        user.setEmail("");

        when(userClient.createUser(user)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/users", user)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void updateUser() {
        UserDto user1 = user;
        user1.setName("dsa");

        when(userClient.updateUser(userId, user1)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/users", user)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void updateUserNotValid() {
        UserDto user1 = user;
        user1.setEmail("");

        when(userClient.updateUser(userId, user1)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/users", user)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void findAllUsers() {
        when(userClient.findAllUsers()).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findUser() {
        when(userClient.findUser(userId)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findUserNotValidId() {
        when(userClient.findUser(-1)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/users/{userId}", -1))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        when(userClient.deleteUser(userId)).thenReturn(objectResponseEntity);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void deleteUserNotValidId() {
        when(userClient.deleteUser(-1)).thenReturn(objectResponseEntity);

        mockMvc.perform(delete("/users/{userId}", -1))
                .andExpect(status().isBadRequest());
    }
}