package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReceived;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestSearch;
import ru.practicum.shareit.request.model.ItemRequestWithItems;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public ItemRequest toRequest(ItemRequestDtoReceived request) {
        return ItemRequest.builder()
                .userId(request.getUserId())
                .description(request.getDescription())
                .build();
    }

    public ItemRequestDto toRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .description(request.getDescription())
                .build();
    }

    public List<ItemRequestDto> toListRequestDto(List<ItemRequest> list) {
        return list.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    public ItemRequestWithItemsDto toRequestWithItemsDto(ItemRequestWithItems request) {
        return ItemRequestWithItemsDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .description(request.getDescription())
                .items(ItemMapper.toListItemInItemDto(request.getItems()))
                .build();
    }

    public List<ItemRequestWithItemsDto> toListRequestWithItemsDto(List<ItemRequestWithItems> list) {
        return list.stream().map(RequestMapper::toRequestWithItemsDto).collect(Collectors.toList());
    }

    public ItemRequestWithItems toItemRequestWithItemsFromItemRequestSearch(ItemRequestSearch request) {
        return ItemRequestWithItems.builder()
                .id(request.getId())
                .created(request.getCreated())
                .description(request.getDescription())
                .build();
    }

    public List<ItemRequestWithItems> toListItemRequestWithItemsFromItemRequestSearch(List<ItemRequestSearch> list) {
        return list.stream().map(RequestMapper::toItemRequestWithItemsFromItemRequestSearch).collect(Collectors.toList());
    }
}
