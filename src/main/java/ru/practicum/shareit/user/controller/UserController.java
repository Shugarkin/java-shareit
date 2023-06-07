package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.marker.Marker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    @Validated({Marker.Create.class})
    public UserDto createUser(@Valid @RequestBody UserDto userDto) { //создание пользователя
        User user = userService.createUser(UserMapper.toItem(userDto));
        return UserMapper.toItemDto(user);
    }

    @PatchMapping("/{userId}")
    @Validated({Marker.Update.class})
    public UserDto updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        User user = userService.updateUser(userId, UserMapper.toItem(userDto));
        return UserMapper.toItemDto(user);
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers().stream().map(UserMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto findUser(@PathVariable Long userId) {
        return UserMapper.toItemDto(userService.findUser(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
