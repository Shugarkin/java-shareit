package ru.practicum.shareit.item.ItemService;

import ru.practicum.shareit.item.model.ItemWithBookingAndComment;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemSearch;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    ItemWithBookingAndComment findItem(Long userId, Long itemId);

    Item updateItem(Long userId, Long itemId, Item itemDto);

    List<ItemWithBookingAndComment> findAllItemByUser(Long userId, int from, int size);

    List<ItemSearch> search(Long userId, String text, int from, int size);

    Comment createComment(Long userId, Long itemId, Comment comment);
}
