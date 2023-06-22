package ru.practicum.shareit.item.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.SmallBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComment;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Item createItem(Long userId, Item item) {
        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        return itemRepository.save(item);
    }

    @Override
    public ItemDtoWithBookingAndComment findItem(Long userId, Long itemId) {
        Item item =  itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
        ItemDtoWithBookingAndComment itemDtoWithBooking = ItemMapper.itemDtoWithBooking(item);

        List<CommentDto> listCommentDto = CommentMapper.toListDto(commentRepository.findAllByItemId(itemId));

        List<Booking> bokklist = bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(itemId, userId, Status.APPROVED);

        final LocalDateTime timeNow = LocalDateTime.now();

        SmallBooking lastBooking = bokklist.stream()
                .filter(a -> !a.getFinish().isAfter(timeNow))
                .map(BookingMapper::toSmallBooking)
                .reduce((a,b) -> b)
                .orElse(null);

        SmallBooking nextBooking = bokklist.stream()
                .filter(a -> a.getFinish().isAfter(timeNow))
                .map(BookingMapper::toSmallBooking)
                .findFirst()
                .orElse(null);

        itemDtoWithBooking.addBooking(lastBooking, nextBooking);
        itemDtoWithBooking.addComments(listCommentDto);
        return itemDtoWithBooking;
    }

    @Transactional
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
        return newItem;
    }

    @Override
    public List<ItemDtoWithBookingAndComment> findAllItemByUser(Long userId) {
        List<Item> listItem = itemRepository.findAllByOwnerId(userId);
        //List<Booking> listBooking = bookingRepository.findAllByItemOwnerIdOrderByStart(userId);

        List<CommentDto> listCommentDto = CommentMapper.toListDto(commentRepository.findAllByUserId(userId));

        Map<Item, List<Booking>> listBooking = bookingRepository.findAllByItemOwnerIdOrderByStart(userId)
				.stream()
                .collect(Collectors.groupingBy(Booking::getItem, Collectors.toList()));


        List<ItemDtoWithBookingAndComment> result = ItemMapper.toListItemDtoWithBooking(listItem);
//        List<ItemDtoWithBookingAndComment> result = List.of();
        final LocalDateTime timeNow = LocalDateTime.now();

//        listItem.stream()
//                            .forEach(item -> {
//                                SmallBooking lastBooking = listBooking.stream()
//                                        .filter(a -> a.getItem().equals(item))
//                                        .filter(a -> !a.getFinish().isAfter(timeNow))
//                                        .reduce((a, b) -> b)
//                                        .map(BookingMapper::toSmallBooking)
//                                        .orElse(null);
//
//                                SmallBooking nextBooking = listBooking.stream()
//                                        .filter(a -> a.getItem().equals(item))
//                                        .filter(a -> a.getFinish().isAfter(timeNow))
//                                        .findFirst()
//                                        .map(BookingMapper::toSmallBooking)
//                                        .orElse(null);
//
//                                //itemDto.addBooking(lastBooking, nextBooking);
//                            });
        List<ItemDtoWithBookingAndComment> res = result.stream()
                        .map(itemDto -> {
                            Item item = listItem.stream()
                                    .filter(item1 -> item1.getId().equals(itemDto.getId()))
                                    .findFirst()
                                    .orElse(null);

                           {
                               SmallBooking lastBooking = listBooking.get(item).stream()
                                        .filter(a -> a.getItem().equals(item))
                                        .filter(a -> !a.getFinish().isAfter(timeNow))
                                        .reduce((a, b) -> b)
                                        .map(BookingMapper::toSmallBooking)
                                        .orElse(null);

                                SmallBooking nextBooking = listBooking.get(item).stream()
                                        .filter(a -> a.getItem().equals(item))
                                        .filter(a -> a.getFinish().isAfter(timeNow))
                                        .findFirst()
                                        .map(BookingMapper::toSmallBooking)
                                        .orElse(null);

                                itemDto.addBooking(lastBooking, nextBooking);
                                    }
                                 return itemDto;

                        })
                                .collect(Collectors.toList());

//        List<Entity1> result = entityList1.stream()
//                .map(entity1 -> {
//                    Entity2 matchingEntity2 = entityList2.stream()
//                            .filter(entity2 -> entity2.getId().equals(entity1.getMatchingId()))
//                            .findFirst()
//                            .orElse(null);
//                    return new Entity1(entity1.getField1(), entity1.getField2(), matchingEntity2.getField1(), matchingEntity2.getField2());
//                })
//                .collect(Collectors.toList());



//        result.stream()
//                .forEach(item -> {
//                    SmallBooking lastBooking = listBooking.stream()
//                            .filter(a -> a.getItem().getId().equals(item.getId()))
//                            .filter(a -> !a.getFinish().isAfter(timeNow))
//        .reduce((a,b) -> b)
//                .map(BookingMapper::toSmallBooking)
//                .orElse(null);
//
//        SmallBooking nextBooking = listBooking.stream()
//                .filter(a -> a.getItem().getId().equals(item.getId()))
//                .filter(a -> a.getFinish().isAfter(timeNow))
//                .findFirst()
//                .map(BookingMapper::toSmallBooking)
//                .orElse(null);
//        item.addBooking(lastBooking, nextBooking);
//    });

        result.stream()
                .forEach(item -> {
        List<CommentDto> list  =
                listCommentDto.stream()
                        .filter(a -> a.getItem().equals(item.getId()))
                        .collect(Collectors.toList());
        item.addComments(list);
    });

        return res;
}

    @Override
    public List<ItemSearch> search(Long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.findItemSearch(text, text);
    }

    @Transactional
    @Override
    public Comment createComment(Long userId, Long itemId, Comment newComment) {
        if (newComment.getText().isBlank()) {
            throw new CommentException("Комментарий не может быть пустым");
        }
        final LocalDateTime timeNow = LocalDateTime.now();
        BookingSearch booking = bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(itemId, userId,
                        Status.APPROVED, timeNow)
                .orElseThrow(() -> new CommentException("Пользователь не бронировал вещь"));
        //по вашему предложению использовать exists - я думаю, что нужно достать сам объект, ибо в ответе нам требуется имя автора
        // комментария, а его можно получить или из booking или делает еще один запрос на получение пользователя
        //хотя возможно я как обычно ошибаюсь
            Comment comment = Comment.builder()
                    .item(booking.getItem())
                    .create(LocalDateTime.now())
                    .user(booking.getBooker())
                    .text(newComment.getText())
                    .build();

            return commentRepository.save(comment);
    }

}
