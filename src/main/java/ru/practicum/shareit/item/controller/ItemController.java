package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") final Long itemId) {
        return itemService.findItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") final Long itemID,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemID, itemDto);
    }

    @GetMapping
    public List<ItemDto> findAllItemByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllItemByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemService.search(userId, text);
    }

}
