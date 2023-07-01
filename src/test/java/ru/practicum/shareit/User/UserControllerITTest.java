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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void findUserTest() {
        long userId = 1L;
        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();

        when(userService.findUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        Mockito.verify(userService, times(2)).findUserById(userId);
    }

    @SneakyThrows
    @Test
    void createUserTest() {
        long userId = 0L;

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
    void notCreateNotValidUserTest() {
        long userId = 0L;

        User userNotValid = User.builder().id(userId).name("dgs").email(null).build();
        mockMvc.perform(post("/users", userNotValid)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userNotValid)))
                .andExpect(status().isBadRequest());

        User userNotValid2 = User.builder().id(userId).name(null).email("fdsjnfj@mail.com").build();
        mockMvc.perform(post("/users", userNotValid2)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userNotValid2)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(userNotValid);
        verify(userService, never()).createUser(userNotValid2);
    }

    @SneakyThrows
    @Test
    void findAllUserTest() {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).findAllUsers();
    }

    @Test
    void deleteUserTest() {
        long userId = 1L;

        userService.deleteUser(userId);

        verify(userService).deleteUser(userId);
    }
}
