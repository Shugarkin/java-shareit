package ru.practicum.shareit.item.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private long nextItemId = 1L;

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Item createItem(Long userId, Item item) {
        checkUser(userId);
        addItem(userId, item);
        return item;
    }

    @Override
    public Item findItem(Long userId, Long itemId) {
        return getItem(userId, itemId);
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
        return searchInMap(userId, text);
    }

    private void checkUser(Long id) {
        List<Long> map = userService.getUserId();
        if (!map.contains(id)) {
            throw new EntityNotFoundException("У вещи должен быть хозяин, а его нет");
        }
    }

    private void addItem(Long userId, Item item) {
        item.setId(nextItemId);
        item.setId(nextItemId++);
        item.setOwner(userId);
        itemStorage.putInMap(userId, Map.of(item.getId(), item));
    }

    private Item upItem(Long userId, Long itemId, Item newItem) {
        try {
            Item item = itemStorage.get(userId).get(itemId);
            if (newItem.getName() == null || newItem.getName().isBlank()) {
                newItem.setName(itemStorage.get(userId).get(itemId).getName());
            } else {
                itemStorage.get(userId).get(itemId).setName(newItem.getName());
            }
            if (newItem.getDescription() == null || newItem.getDescription().isBlank()) {
                newItem.setDescription(itemStorage.get(userId).get(itemId).getDescription());
            } else {
                itemStorage.get(userId).get(itemId).setDescription(newItem.getDescription());
            }
            if (newItem.getAvailable() == null) {
                newItem.setAvailable(itemStorage.get(userId).get(itemId).getAvailable());
            } else {
                itemStorage.get(userId).get(itemId).setAvailable(newItem.getAvailable());
            }
            return item;
        } catch (RuntimeException e) {
            throw new EntityNotFoundException("Не найдено");
        }
    }

    private Item getItem(Long userId, Long itemId) {
        List<Item> list = itemStorage.values()
                .stream()
                .filter(s -> s.containsKey(itemId))
                .map(a -> a.values())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());;
        return list.get(0);
    }

    private List<Item> getAllItemByUser(Long userId) {
        List<Item> list = itemStorage.get(userId).values()
                .stream()
                .collect(Collectors.toList());
        return list;
    }

    private List<Item> searchInMap(Long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        String lowText = text.toLowerCase();
        List<Item> list = itemStorage.values()
                .stream()
                .map(a -> a.values())
                .flatMap(Collection::stream)
                .filter(a -> a.getAvailable() == true)
                .filter(a -> Stream.of(a.getDescription().toLowerCase(), a.getName().toLowerCase())
                        .anyMatch(s -> s.contains(lowText)))
                .collect(Collectors.toList());

        return list;
    }
}
