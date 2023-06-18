package ru.practicum.shareit.item.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingApproved;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.UselessBooking;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    @Override
    public Item createItem(Long userId, Item item) {
        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        return itemRepository.save(item);
    }

    @Override
    public ItemDtoWithBooking findItem(Long userId, Long itemId) {
        Item item =  itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
        ItemDtoWithBooking itemDtoWithBooking = ItemMapper.itemDtoWithBooking(item);
        List<Booking> list = bookingRepository.findAllByItemIdAndItemOwnerIdOrderByStart(itemId, userId);
        if (list.isEmpty()) {
            return itemDtoWithBooking;
        } else if (list.size() == 1) {
            itemDtoWithBooking.setLastBooking(UselessBooking.builder()
                    .id(list.get(0).getId())
                    .bookerId(list.get(0).getBooker().getId())
                    .build());
        } else {
            itemDtoWithBooking.setLastBooking(UselessBooking.builder()
                    .id(list.get(0).getId())
                    .bookerId(list.get(0).getBooker().getId())
                    .build());
            itemDtoWithBooking.setNextBooking(UselessBooking.builder()
                    .id(list.get(1).getId())
                    .bookerId(list.get(1).getBooker().getId())
                    .build());
        }
        return itemDtoWithBooking;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        Item newItem = Optional.of(itemRepository.findByIdAndOwnerId(itemId, userId))
                .orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
        String name = item.getName();
        String description = item.getDescription();
        Boolean available = item.getAvailable();
        if (name != null && !name.isBlank()) {
            newItem.setName(name);
        }
        if (description != null && !description.isBlank()) {
            newItem.setDescription(description);
        }
        if (available != null) {
            newItem.setAvailable(available);
        }
        return itemRepository.save(newItem);
    }

    @Override
    public List<Item> findAllItemByUser(Long userId) {
        List<Item> list = itemRepository.findAllByOwnerId(userId);
        List<Booking> list1 = bookingRepository.findAllByItemOwnerIdOrderByStart(userId);
//        List<UselessBooking> uList = BookingMapper.toListUselessBooking(list1);
//        Map<Long, UselessBooking> mapBooking = uList.stream().collect(Collectors.toMap(UselessBooking::getId, Function.identity()));
//        List<ItemDtoWithBooking> listItem = list.stream().map(ItemMapper::itemDtoWithBooking).collect(Collectors.toList());
//
//        for (ItemDtoWithBooking itemDtoWithBooking : listItem) {
//            for (UselessBooking uselessBooking : uList) {
//                if (uselessBooking.getId().equals(itemDtoWithBooking.getId())) {
//
//                }
//            }
//        }
//
//        List<UselessBooking> uList = BookingMapper.toListUselessBooking(list1);
//        List<ItemDtoWithBooking> listItem = list.stream().map(ItemMapper::itemDtoWithBooking).collect(Collectors.toList());
//        List<ItemDtoWithBooking> listItem1 = listItem.stream()
//                .map(a -> {UselessBooking u = uList.stream()
//                        .filter(s -> s.getId().equals(a.getId()))
//                        .findFirst()
//                        .orElse(null);
//                        a.setLastBooking(u);
//                        return a;
//                }).collect(Collectors.toList());

//        List<UselessBooking> uList = BookingMapper.toListUselessBooking(list1);
//        List<ItemDtoWithBooking> listItem = list.stream().map(ItemMapper::itemDtoWithBooking)
//                .map(a -> {Booking booking = list1.stream().filter(s -> s.getItem().getId().equals(a.getId()));
//                a.setLastBooking(new UselessBooking(booking.getId(), booking.getBooker().getId()));})
        return null;
    }

    @Override
    public List<ItemSearch> search(Long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        String newText = text.toLowerCase();
        return itemRepository.findItemSearch(newText, newText);
    }

}
