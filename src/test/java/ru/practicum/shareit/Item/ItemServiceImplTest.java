package ru.practicum.shareit.Item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Equals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemService.ItemServiceImpl;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentReceiving;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingAndComment;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    void createItemTest() {
        long userId = 1L;

        User user = User.builder().build();

        Item item = Item.builder().build();

        when(itemRepository.save(item)).thenReturn(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Item newItem = itemService.createItem(userId, item);

        assertEquals(newItem, item);
    }

    @Test
    void findItemTest() {
        long userId = 1L;
        long itemId = 1L;

        Item item = Item.builder().build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        when(commentRepository.findAllByItemId(itemId)).thenReturn(List.of());

        when(bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(itemId, userId, Status.APPROVED)).thenReturn(List.of());

        ItemWithBookingAndComment itemWithBookingAndComment = itemService.findItem(userId, itemId);

        ItemWithBookingAndComment itemWithBookingAndCommentNew = ItemMapper.itemWithBooking(item);
        itemWithBookingAndCommentNew.setComments(List.of());

        assertEquals(itemWithBookingAndComment, itemWithBookingAndCommentNew);
    }

    @Test
    void findItemNotEntityTest() {
        long itemId = 1L;
        long userId = 1L;

        when(itemRepository.findById(itemId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemService.findItem(userId, itemId));
    }

    @Test
    void updateItemTest() {
        long itemId = 1L;
        long userId = 1L;
        Item item = Item.builder().build();
        Item newItem = Item.builder().name("asd").build();

        when(itemRepository.findByIdAndOwnerId(itemId, userId)).thenReturn(Optional.of(item));

        Item itemUpdate = itemService.updateItem(userId, itemId, newItem);

        assertEquals(itemUpdate, newItem);
        assertEquals(itemUpdate, item);
    }

    @Test
    void updateItemNotEntityTest() {
        long itemId = 1L;
        long userId = 1L;
        Item item = Item.builder().build();

        when(itemRepository.findByIdAndOwnerId(itemId, userId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(userId, itemId, item));
        verify(itemRepository, Mockito.never()).save(item);
    }

    @Test
    void findAllItemByUserTest() {
        long userId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from, size);
        List<Item> itemLIst = List.of(Item.builder().build());
        when(itemRepository.findAllByOwnerId(userId, pageable)).thenReturn(itemLIst);

        List<Comment> commentList = List.of();
        when(commentRepository.findAllByUserId(userId)).thenReturn(commentList);

        List<Booking> bookingList = List.of();
        when(bookingRepository.findAllByItemOwnerIdOrderByStart(userId)).thenReturn(bookingList);



        List<ItemWithBookingAndComment> itemWithLIst = itemLIst.stream()
                .map(a -> ItemMapper.itemWithBooking(a)).collect(Collectors.toList());

        Map<Long, List<CommentReceiving>> listComment = commentList
                .stream()
                .map(a -> CommentMapper.fromCommentToCommentReceiving(a))
                .collect(Collectors.groupingBy(c -> c.getItem(), Collectors.toList()));

        Map<Long, List<Booking>> listBooking = bookingList
                .stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId(), Collectors.toList()));

        itemWithLIst.stream()
                .forEach(item -> {
                    List<CommentReceiving> list  =
                            listComment.getOrDefault(item.getId(), List.of());

                    item.addComments(list);
                });


        List<ItemWithBookingAndComment> result = itemService.findAllItemByUser(userId, from, size);

        assertEquals(result, itemWithLIst);
    }

    @Test
    void searchTest() {
        long userId = 1L;
        String text = "asd";
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from, size);

        List<ItemSearch> itenList = List.of(new ItemSearch());

        when(itemRepository.findItemSearch(text, text, pageable)).thenReturn(itenList);

        List<ItemSearch> newList = itemService.search(userId, text, from, size);

        assertEquals(newList, itenList);
    }

    @Test
    void createCommentTest() {
        long userId = 1L;
        long itemId = 1L;
        int from = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(from, size);

        Comment newComment = Comment.builder().build();
        LocalDateTime time = LocalDateTime.now();

        BookingSearch booking = new BookingSearch();

        when(bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(itemId, userId,
                Status.APPROVED, time, pageable)).thenReturn(List.of(booking));

        Comment comment = itemService.createComment(userId, itemId, newComment);

        newComment.setCreate(time);
        verify(commentRepository).save(newComment);
    }

    @Test
    void createCommentNotEntityTest() {
        long userId = 1L;
        long itemId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from, size);

        Comment newComment = Comment.builder().build();
        LocalDateTime time = LocalDateTime.now();

        when(bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(itemId, userId,
                Status.APPROVED, time, pageable)).thenThrow(CommentException.class);

        assertThrows(CommentException.class, () -> itemService.createComment(userId, itemId, newComment));
        verify(commentRepository, never()).save(newComment);

    }

}
