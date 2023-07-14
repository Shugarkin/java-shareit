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

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                     @RequestBody @Valid ItemRequestDtoReceived requestDto) {
        ItemRequest request = itemRequestService.addRequest(RequestMapper.toRequest(requestDto), userId);
        return RequestMapper.toRequestDto(request);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> findListRequestUser(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId) {
        List<ItemRequestWithItems> list = itemRequestService.findListRequestUser(userId);
        return RequestMapper.toListRequestWithItemsDto(list);
    }

    @GetMapping("/all")
    public List<ItemRequestWithItemsDto> findListRequest(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                                         @Min(0) @RequestParam(defaultValue = "0")  int from,
                                                         @Min(1) @RequestParam(defaultValue = "10")  int size) {
        return RequestMapper.toListRequestWithItemsDto(itemRequestService.findListRequest(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto findItemRequest(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                                   @PathVariable("requestId") @Min(0) long requestId) {

        return RequestMapper.toRequestWithItemsDto(itemRequestService.findItemRequest(userId, requestId));
    }
}
