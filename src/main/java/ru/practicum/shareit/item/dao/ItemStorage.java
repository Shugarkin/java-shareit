package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class ItemStorage {

    private Map<Long, List<Item>> itemMap = new HashMap<>();

    private Map<Long, Item> onlyItemMap = new HashMap<>();

    private long nextItemId = 1L;

    public void putInMap(Long id, Item item) {
        item.setId(nextItemId++);
        item.setOwner(id);
        final List<Item> items = itemMap.computeIfAbsent(id, k -> new ArrayList<>());
        items.add(item);
        onlyItemMap.put(item.getId(), item);
        itemMap.put(id, items);
    }

    public Item getItem(Long userId, Long itemId) {
        List<Item> list = itemMap.getOrDefault(userId, new ArrayList<>())
                .stream()
                .filter(a -> a.getId() == itemId)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        return list.get(0);
    }

    public Item getItemToSee(Long itemId) {
        return onlyItemMap.get(itemId);
    }

    public List<Item> getItemsByUserId(Long id) {
        return itemMap.get(id);
    }

    public List<Item> searchInMap(Long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        String lowText = text.toLowerCase();
        List<Item> list = onlyItemMap.values()
                .stream()
                .filter(a -> a.getAvailable() == true && Stream.of(a.getDescription().toLowerCase(), a.getName().toLowerCase())
                        .anyMatch(s -> s.contains(lowText)))
                .collect(Collectors.toList());

        return list;
    }
}
