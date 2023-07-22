package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemRequestDtoReceived {

    private Long userId;

    private String description;


}
