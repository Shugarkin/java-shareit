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
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public Item createItem(Long userId, Item item) {
        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        return itemRepository.save(item);
    }

    @Override
    public ItemDtoWithBooking findItem(Long userId, Long itemId) {
        Item item =  itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
        ItemDtoWithBooking itemDtoWithBooking = ItemMapper.itemDtoWithBooking(item);

        List<CommentDto> listCommentDto = CommentMapper.toListDto(commentRepository.findAllByItemIdAndUserId(itemId, userId));

        List<Booking> bokklist = bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusNot(itemId, userId, Status.REJECTED);
        //bokklist.stream().filter(a -> a.getFinish().isBefore(LocalDateTime.now()));
        //lastBooling это последнее добавленное а nextBooking это ближайшее по времени
        List<UselessBooking> list = BookingMapper
                .toListUselessBooking(bokklist);

        itemDtoWithBooking.addBooking(list);
        itemDtoWithBooking.addComments(listCommentDto);
        return itemDtoWithBooking;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        Item newItem = itemRepository.findByIdAndOwnerId(itemId, userId)
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

        List<CommentDto> listCommentDto = CommentMapper.toListDto(commentRepository.findAllByUserId(userId));

        List<ItemDtoWithBooking> result = ItemMapper.toListItemDtoWithBooking(listItem);
        result.stream()
                .forEach(item -> {List<UselessBooking> list =
                    uList.stream()
                            .filter(a -> a.getItemId().equals(item.getId()))
                            .collect(Collectors.toList());
                            item.addBooking(list);
                });
        result.stream()
                .forEach(item -> {List<CommentDto> list  =
                    listCommentDto.stream()
                            .filter(a -> a.getItem().equals(item.getId()))
                            .collect(Collectors.toList());
                            item.addComments(list);});

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

    @Override
    public Comment createComment(Long userId, Long itemId, String text) {
        if (text.isBlank()) {
            throw new EntityNotFoundException("Пока так 2");
        }
        Booking booking = bookingRepository.findByItemIdAndBookerId(itemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не бронировал вещь"));
        if (booking.getFinish().isBefore(LocalDateTime.now())) {
            Comment comment = Comment.builder()
                    .item(booking.getItem())
                    .create(LocalDateTime.now())
                    .user(booking.getBooker())
                    .text(text)
                    .build();
            return comment;
        } else {
            throw new EntityNotFoundException("Пока так");
        }
    }


}
