package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Builder
public class ItemRequestDtoReceived {

    private Long userId;

    @NotBlank
    @Size(min = 0, max = 255)
    private String description;


}
