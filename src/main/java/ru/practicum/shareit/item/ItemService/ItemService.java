package ru.practicum.shareit.item.ItemService;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto findItem(Long userId, Long itemId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> findAllItemByUser(Long userId);

    List<ItemDto> search(Long userId, String text);
}
