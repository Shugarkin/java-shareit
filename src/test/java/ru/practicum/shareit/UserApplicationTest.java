package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@SpringBootTest

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserApplicationTest {

    private final UserService userService;

    private User user;

    @BeforeEach
    public void before() {
        user = userService.createUser(User.builder()
                .name("Викинг")
                .email("viking@mail.com")
                .build());
        Assertions.assertNotNull(user);
    }

    @AfterEach
    public void after() {
        userService.deleteUser(user.getId());
    }

    @Test
    public void test() {

        User newUser = userService.findUserById(user.getId());
        Assertions.assertNotNull(newUser);

        List<User> list = userService.findAllUsers();
        Assertions.assertNotNull(list);

        User newNewUser = userService.updateUser(user.getId(), User.builder().name("viking").build());
        Assertions.assertNotNull(newNewUser);
    }
}
