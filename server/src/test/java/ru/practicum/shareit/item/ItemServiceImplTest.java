package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.SmallBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemService.ItemServiceImpl;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
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
import java.util.Optional;

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


    private long userId = 1L;
    private long itemId = 1L;

    @Test
    void createItem() {

        User user = User.builder().build();

        Item item = Item.builder().build();

        when(itemRepository.save(item)).thenReturn(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Item newItem = itemService.createItem(userId, item);

        assertEquals(newItem, item);
    }

    @Test
    void findItem() {

        Item item = Item.builder().build();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        when(commentRepository.findAllByItemId(itemId)).thenReturn(List.of());

        when(bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(itemId, userId, Status.APPROVED)).thenReturn(List.of());

        ItemWithBookingAndComment itemWithBookingAndComment = itemService.findItem(userId, itemId);

        ItemWithBookingAndComment itemWithBookingAndCommentNew = ItemMapper.itemWithBooking(item);

        Booking booking = Booking.builder()
                .id(1L)
                .item(Item.builder().id(1L).build())
                .booker(User.builder().id(1L).build())
                .build();

        SmallBooking smallBooking = BookingMapper.toSmallBooking(booking);

        itemWithBookingAndComment.addBooking(smallBooking, smallBooking);

        itemWithBookingAndCommentNew.addBooking(smallBooking, smallBooking);
        itemWithBookingAndCommentNew.setComments(List.of());

        assertEquals(itemWithBookingAndComment, itemWithBookingAndCommentNew);
    }

    @Test
    void findItemNotEntity() {

        when(itemRepository.findById(itemId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemService.findItem(userId, itemId));
    }

    @Test
    void updateItem() {
        Item item = Item.builder().build();
        Item newItem = Item.builder().name("asd").build();

        when(itemRepository.findByIdAndOwnerId(itemId, userId)).thenReturn(Optional.of(item));

        Item itemUpdate = itemService.updateItem(userId, itemId, newItem);

        assertEquals(itemUpdate, newItem);
        assertEquals(itemUpdate, item);
    }

    @Test
    void updateItemNotEntity() {
        Item item = Item.builder().build();

        when(itemRepository.findByIdAndOwnerId(itemId, userId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(userId, itemId, item));
        verify(itemRepository, Mockito.never()).save(item);
    }

    @Test
    void findAllItemByUser() {
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from, size);
        Item item = Item.builder().id(1L).build();
        ItemWithBookingAndComment itemWithBookingAndComment = ItemMapper.itemWithBooking(item);
        itemWithBookingAndComment.setComments(List.of());

        List<Item> itemLIst = List.of(item);
        when(itemRepository.findAllByOwnerId(userId, pageable)).thenReturn(itemLIst);

        List<Comment> commentList = List.of();
        when(commentRepository.findAllByUserId(userId)).thenReturn(commentList);

        List<Booking> bookingList = List.of();
        when(bookingRepository.findAllByItemOwnerIdOrderByStart(userId)).thenReturn(bookingList);

        List<ItemWithBookingAndComment> result = itemService.findAllItemByUser(userId, from, size);

        assertEquals(result, List.of(itemWithBookingAndComment));
    }

    @Test
    void search() {
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
    void createComment() {
        int from = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(from, size);

        Comment newComment = Comment.builder()
                .id(1L)
                .create(LocalDateTime.now())
                .text("asd")
                .item(Item.builder().id(1L).build())
                .user(User.builder().name("a").build())
                .build();

        BookingSearch booking = new BookingSearch();

        when(bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(itemId, userId,
                Status.APPROVED, pageable)).thenReturn(List.of(booking));

        when(commentRepository.save(any())).thenReturn(newComment);

        Comment comment = itemService.createComment(userId, itemId, newComment);

        CommentReceiving commentReceiving = CommentMapper.fromCommentToCommentReceiving(comment);
        CommentDto commentDto = CommentMapper.fromCommentRecrivingToCommentDto(commentReceiving);

        CommentReceiving commentReceiving1 = CommentMapper.fromCommentToCommentReceiving(newComment);
        CommentDto commentDto1 = CommentMapper.fromCommentRecrivingToCommentDto(commentReceiving1);

        assertEquals(commentDto1, commentDto);
    }

    @Test
    void createCommentNotEntity() {
        int from = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(from, size);

        Comment newComment = Comment.builder().build();

        when(bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(itemId, userId,
                Status.APPROVED, pageable)).thenReturn(List.of());

        assertThrows(CommentException.class, () -> itemService.createComment(userId, itemId, newComment));
        verify(commentRepository, never()).save(newComment);

    }


}
