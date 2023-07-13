package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComment;
import ru.practicum.shareit.item.model.ItemWithBookingAndComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemSearch;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public ItemDto itemSerchToItemDto(ItemSearch item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public Item toItem(ItemDto item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public List<ItemDto> toListItemSearchInItemDto(List<ItemSearch> listItem) {
        return listItem.stream().map(ItemMapper::itemSerchToItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> toListItemInItemDto(List<Item> listItem) {
        return listItem.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public ItemDtoWithBookingAndComment itemDtoWithBooking(ItemWithBookingAndComment item) {
        return ItemDtoWithBookingAndComment.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking())
                .nextBooking(item.getNextBooking())
                .comments(CommentMapper.commentDtoList(item.getComments()))
                .build();
    }

    public List<ItemDtoWithBookingAndComment> toListItemDtoWithBooking(List<ItemWithBookingAndComment> list) {
        return list.stream().map(ItemMapper::itemDtoWithBooking).collect(Collectors.toList());
    }

    public ItemWithBookingAndComment itemWithBooking(Item item) {
        return ItemWithBookingAndComment.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}
