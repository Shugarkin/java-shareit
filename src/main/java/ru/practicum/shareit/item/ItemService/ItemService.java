package ru.practicum.shareit.item.ItemService;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemSearch;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    ItemDtoWithBooking findItem(Long userId, Long itemId);

    Item updateItem(Long userId, Long itemId, Item itemDto);

    List<ItemDtoWithBooking> findAllItemByUser(Long userId);

    List<ItemSearch> search(Long userId, String text);

    Comment createComment(Long userId, Long itemId, String text);
}
