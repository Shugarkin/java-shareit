package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.Create.class}) @RequestBody ItemDto itemDto) {
        Item item = itemService.createItem(userId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItem(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId, @PathVariable("itemId") final Long itemId) {
        Item item = itemService.findItem(userId, itemId);
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId, @PathVariable("itemId") final Long itemId,
                              @Validated({Marker.Update.class}) @RequestBody ItemDto itemDto) {
        Item item = itemService.updateItem(userId, itemId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> findAllItemByUser(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId) {
        List<Item> listItem = itemService.findAllItemByUser(userId);
        return ItemMapper.toListItemDto(listItem);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId, @RequestParam String text) {
        List<Item> itemList = itemService.search(userId, text);
        return ItemMapper.toListItemDto(itemList);
    }

}
