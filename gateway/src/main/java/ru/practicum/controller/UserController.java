package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.UserClient;
import ru.practicum.dto.Marker;
import ru.practicum.dto.UserDto;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(Marker.Create.class) UserDto userDto) { //создание пользователя
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @Min(1) Long userId,
                                             @Validated({Marker.Update.class}) @RequestBody UserDto userDto) {
        return userClient.updateUser(userId, userDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        return userClient.findAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUser(@PathVariable @Min(1) Long userId) {
        return userClient.findUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Min(1) Long userId) {
        userClient.deleteUser(userId);
    }
}
