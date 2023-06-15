package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private Long userId;

    private LocalDateTime created;
}
