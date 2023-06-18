package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
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
                .build();
    }

    public ItemDto ItemSerchToItemDto(ItemSearch item) {
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
                .build();
    }

    public List<ItemDto> toListItemDto(List<Item> listItem) {
        return listItem.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> toListItemSearchInItemDto(List<ItemSearch> listItem) {
        return listItem.stream().map(ItemMapper::ItemSerchToItemDto).collect(Collectors.toList());
    }

    public ItemDtoWithBooking itemDtoWithBooking(Item item) {
        return ItemDtoWithBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

//    public List<ItemDtoWithBooking> toListItemDtoWithBooking(List<Item> list) {
//        return list.stream().map(ItemMapper::itemDtoWithBooking).collect(Collectors.toList());
//    }
}
