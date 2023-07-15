package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDtoReceived;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                             @RequestBody @Valid ItemRequestDtoReceived requestDto) {
        return itemRequestClient.addRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findListRequestUser(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId) {
        return itemRequestClient.findListRequestUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findListRequest(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                                         @Min(0) @RequestParam(defaultValue = "0")  int from,
                                                         @Min(1) @RequestParam(defaultValue = "10")  int size) {
        return itemRequestClient.findListRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequest(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                                   @PathVariable("requestId") @Min(0) long requestId) {

        return itemRequestClient.findItemRequest(userId, requestId);
    }
}
