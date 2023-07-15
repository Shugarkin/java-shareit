package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReceived;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody ItemRequestDtoReceived requestDto) {
        ItemRequest request = itemRequestService.addRequest(RequestMapper.toRequest(requestDto), userId);
        return RequestMapper.toRequestDto(request);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> findListRequestUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemRequestWithItems> list = itemRequestService.findListRequestUser(userId);
        return RequestMapper.toListRequestWithItemsDto(list);
    }

    @GetMapping("/all")
    public List<ItemRequestWithItemsDto> findListRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam int from,
                                                         @RequestParam int size) {
        return RequestMapper.toListRequestWithItemsDto(itemRequestService.findListRequest(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto findItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PathVariable("requestId") long requestId) {

        return RequestMapper.toRequestWithItemsDto(itemRequestService.findItemRequest(userId, requestId));
    }
}
