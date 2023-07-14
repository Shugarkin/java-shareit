package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                              @Validated({Marker.Create.class}) @RequestBody ItemDto itemDto) {
        Item item = itemService.createItem(userId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingAndComment findItem(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                                 @PathVariable("itemId") @Min(0) final Long itemId) {
        ItemDtoWithBookingAndComment item = ItemMapper.itemDtoWithBooking(itemService.findItem(userId, itemId));
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId, @PathVariable("itemId") @Min(0) final Long itemId,
                              @Validated({Marker.Update.class}) @RequestBody ItemDto itemDto) {
        Item item = itemService.updateItem(userId, itemId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDtoWithBookingAndComment> findAllItemByUser(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                                                @RequestParam(defaultValue = "0") @Min(0)  int from,
                                                                @RequestParam(defaultValue = "10") @Min(1)  int size) {
        List<ItemDtoWithBookingAndComment> listItem = ItemMapper.toListItemDtoWithBooking(itemService.findAllItemByUser(userId, from, size));
        return listItem;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId, @RequestParam String text,
                                @RequestParam(defaultValue = "0") @Min(0)  int from,
                                @RequestParam(defaultValue = "10") @Min(1)  int size) {
        List<ItemSearch> itemList = itemService.search(userId, text, from, size);
        return ItemMapper.toListItemSearchInItemDto(itemList);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                    @PathVariable("itemId") @Min(0) final Long itemId,
                                    @Validated({Marker.Create.class}) @RequestBody CommentDtoReceived newComment) {
        Comment comment = itemService.createComment(userId, itemId, CommentMapper.fromCommentDtoReceivedToComment(newComment));
        return CommentMapper.toCommentDto(comment);
    }

}
