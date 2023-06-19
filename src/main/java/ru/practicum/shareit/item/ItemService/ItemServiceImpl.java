package ru.practicum.shareit.item.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingApproved;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.EntityBooking;
import ru.practicum.shareit.booking.model.Status;
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
        List<UselessBooking> list = BookingMapper.toListUselessBooking(bookingRepository.findAllByItemIdAndItemOwnerIdOrderByStart(itemId, userId, Status.REJECTED));
        itemDtoWithBooking.add(list);
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
    public List<ItemDtoWithBooking> findAllItemByUser(Long userId) {
        List<Item> listItem = itemRepository.findAllByOwnerId(userId);
        List<Booking> list1 = bookingRepository.findAllByItemOwnerIdOrderByStart(userId);

        List<UselessBooking> uList = BookingMapper.toListUselessBooking(list1);


        List<ItemDtoWithBooking> result = ItemMapper.toListItemDtoWithBooking(listItem);
        result.stream()
                .forEach(item -> {List<UselessBooking> list =
                    uList.stream()
                            .filter(a -> a.getItemId().equals(item.getId()))
                            .collect(Collectors.toList());
                            item.add(list);
                });
        return result;
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
