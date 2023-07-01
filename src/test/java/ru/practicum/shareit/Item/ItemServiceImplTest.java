package ru.practicum.shareit.Item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Equals;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemService.ItemServiceImpl;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.CommentReceiving;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingAndComment;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
    }



}
