package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.ItemClient;
import ru.practicum.dto.CommentDtoReceived;
import ru.practicum.dto.ItemDto;
import ru.practicum.dto.Marker;

import javax.validation.constraints.Min;
import java.util.List;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                             @Validated({Marker.Create.class}) @RequestBody ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItem(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                                 @PathVariable("itemId") @Min(0) final Long itemId) {
        return itemClient.findItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId, @PathVariable("itemId") @Min(0) final Long itemId,
                              @Validated({Marker.Update.class}) @RequestBody ItemDto itemDto) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemByUser(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                                                @RequestParam(defaultValue = "0") @Min(0)  int from,
                                                                @RequestParam(defaultValue = "10") @Min(1)  int size) {
        return itemClient.findAllItemByUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId, @RequestParam String text,
                                @RequestParam(defaultValue = "0") @Min(0)  int from,
                                @RequestParam(defaultValue = "10") @Min(1)  int size) {
        boolean answer = text.isBlank();
        if (answer) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }

        return itemClient.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                    @PathVariable("itemId") @Min(0) final Long itemId,
                                    @Validated({Marker.Create.class}) @RequestBody CommentDtoReceived newComment) {
        return itemClient.createComment(userId, itemId, newComment);
    }

}
