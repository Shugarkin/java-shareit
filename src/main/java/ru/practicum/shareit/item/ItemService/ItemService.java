package ru.practicum.shareit.item.ItemService;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    Item findItem(Long userId, Long itemId);

    Item updateItem(Long userId, Long itemId, Item itemDto);

    List<Item> findAllItemByUser(Long userId);

    List<Item> search(Long userId, String text);
}
