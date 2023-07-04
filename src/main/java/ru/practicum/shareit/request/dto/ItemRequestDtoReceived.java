package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
public class ItemRequestDtoReceived {

    private Long userId;

    @NotBlank
    private String description;


}
