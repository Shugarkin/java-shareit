package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
public class ItemStorage {

    private Map<Long, Map<Long, Item>> itemMap = new HashMap<>();

    public void putInMap(Long id, Map<Long, Item> map) {
        itemMap.put(id, map);
    }

    public Map<Long, Item> get(Long id) {
        return itemMap.get(id);
    }

    public Collection<Map<Long, Item>> values() {
        return itemMap.values();
    }
}
