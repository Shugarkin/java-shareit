package ru.practicum.shareit.item.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private long nextItemId = 1L;

    private Map<Long, Map<Long, Item>> map = new HashMap<>();

    private final ItemMapper itemMapper;

    private final UserService userService;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        checkUser(userId);
        addItem(userId, itemDto);
        return itemDto;
    }

    @Override
    public ItemDto findItem(Long userId, Long itemId) {
        return getItem(userId, itemId);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        ItemDto newDto = itemMapper.toItemDto(upItem(userId, itemId, itemDto));
        return newDto;
    }

    @Override
    public List<ItemDto> findAllItemByUser(Long userId) {
        return getAllItemByUser(userId);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        return searchInMap(userId, text);
    }

    private void checkUser(Long id) {
        List<Long> map = userService.getUserId();
        if (!map.contains(id)) {
            throw new UserNotFoundException("У вещи должен быть хозяин, а его нет");
        }
    }

    private void addItem(Long userId, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        itemDto.setId(nextItemId);
        item.setId(nextItemId++);
        item.setOwner(userId);
        map.put(userId, Map.of(item.getId(), item));
    }

    private Item upItem(Long userId, Long itemId, ItemDto itemDto) {
        try {
            Item item = map.get(userId).get(itemId);
            Item newItem = itemMapper.toItem(itemDto);
            if (newItem.getName() != null) {
                item.setName(newItem.getName());
            }
            if (newItem.getDescription() != null) {
                item.setDescription(newItem.getDescription());
            }
            if (newItem.getAvailable() != null) {
                item.setAvailable(newItem.getAvailable());
            }
            return item;
        } catch (RuntimeException e) {
            throw new UserNotFoundException("Не найдено");
        }
    }

    private ItemDto getItem(Long userId, Long itemId) {
        List<Item> list = map.values()
                .stream()
                .filter(s -> s.containsKey(itemId))
                .map(a -> a.values())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        ItemDto itemDto = itemMapper.toItemDto(list.get(0));
        return itemDto;
    }

    private List<ItemDto> getAllItemByUser(Long userId) {
        List<ItemDto> list = map.get(userId).values()
                .stream()
                .map(this.itemMapper::toItemDto)
                .collect(Collectors.toList());
        return list;
    }

    private List<ItemDto> searchInMap(Long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        String lowText = text.toLowerCase();
        List<ItemDto> list = map.values()
                .stream()
                .map(a -> a.values())
                .flatMap(Collection::stream)
                .filter(a -> a.getAvailable() == true)
                .filter(a -> Stream.of(a.getDescription().toLowerCase(), a.getName().toLowerCase())
                        .anyMatch(s -> s.contains(lowText)))
                .map(this.itemMapper::toItemDto)
                .collect(Collectors.toList());

        return list;
    }
}
