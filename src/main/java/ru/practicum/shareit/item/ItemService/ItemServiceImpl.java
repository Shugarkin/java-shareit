package ru.practicum.shareit.item.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    @Override
    public Item createItem(Long userId, Item item) {
        checkUser(userId);
        addItem(userId, item);
        return item;
    }

    @Override
    public Item findItem(Long userId, Long itemId) {
        return itemStorage.getItemToSee(itemId);
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        return upItem(userId, itemId, item);
    }

    @Override
    public List<Item> findAllItemByUser(Long userId) {
        return getAllItemByUser(userId);
    }

    @Override
    public List<Item> search(Long userId, String text) {
        return itemStorage.searchInMap(userId, text);
    }

    private void checkUser(Long id) {
        if (!userStorage.existById(id)) {
            throw new EntityNotFoundException("У вещи должен быть хозяин, а его нет");
        }
    }

    private void addItem(Long userId, Item item) {
        itemStorage.putInMap(userId, item);
    }

    private Item upItem(Long userId, Long itemId, Item newItem) {
            Item item = itemStorage.getItem(userId, itemId);
            if (item != null) {
                String name = newItem.getName();
                String description = newItem.getDescription();
                Boolean available = newItem.getAvailable();
                if (name != null && !name.isBlank()) {
                    item.setName(name);
                }
                if (description != null && !description.isBlank()) {
                    item.setDescription(description);
                }
                if (available != null) {
                    item.setAvailable(available);
                }
            } else {
                throw new EntityNotFoundException("Пользователь не найден");
            }
            return item;
    }

    private List<Item> getAllItemByUser(Long userId) {
        List<Item> list = itemStorage.getItemsByUserId(userId)
                .stream()
                .collect(Collectors.toList());
        return list;
    }
}
