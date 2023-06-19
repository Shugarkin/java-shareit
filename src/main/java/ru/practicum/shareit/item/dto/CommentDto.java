package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Long id;

    @NotBlank
    private String text;

    @NotNull
    private Long item;

    @NotNull
    private Long user;

    @FutureOrPresent
    private LocalDateTime create;

}
