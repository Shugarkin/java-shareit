package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestWithItems {
    private Long id;

    private String description;

    private LocalDateTime created;

    private List<Item> items;

}
