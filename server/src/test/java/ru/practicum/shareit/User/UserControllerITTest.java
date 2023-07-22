package ru.practicum.shareit.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    private long userId = 1L;

    @SneakyThrows
    @Test
    void findUser() {
        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();

        when(userService.findUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        Mockito.verify(userService, times(1)).findUserById(userId);
    }

    @SneakyThrows
    @Test
    void createUser() {

        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        when(userService.createUser(user)).thenReturn(user);

        String userString = mockMvc.perform(post("/users", user)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        Assertions.assertEquals(userString, objectMapper.writeValueAsString(user));
    }

    @SneakyThrows
    @Test
    void findAllUser() {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).findAllUsers();
    }

    @Test
    void deleteUser() {

        userService.deleteUser(userId);

        verify(userService).deleteUser(userId);
    }

    @SneakyThrows
    @Test
    void updateUser() {
        long userId = 1L;

        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        when(userService.updateUser(userId, user)).thenReturn(user);

        String userString = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(userString, objectMapper.writeValueAsString(user));
    }
}
