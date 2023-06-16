package ru.practicum.shareit.item.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemSearch;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public Item createItem(Long userId, Item item) {
        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        return itemRepository.save(item);
    }

    @Override
    public Item findItem(Long userId, Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        Item newItem = Optional.of(itemRepository.findByIdAndOwnerId(itemId, userId))
                .orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
        String name = item.getName();
        String description = item.getDescription();
        Boolean available = item.getAvailable();
        if (name != null && !name.isBlank()) {
            newItem.setName(name);
        }
        if (description != null && !description.isBlank()) {
            newItem.setDescription(description);
        }
        if (available != null) {
            newItem.setAvailable(available);
        }
        return itemRepository.save(newItem);
    }

    @Override
    public List<Item> findAllItemByUser(Long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    public List<ItemSearch> search(Long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        String newText = text.toLowerCase();
        return itemRepository.findItemSearch(newText, newText);
    }

}
