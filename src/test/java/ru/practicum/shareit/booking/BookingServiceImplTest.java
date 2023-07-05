package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void postBookingTest() {
        long userId = 1L;
        long itemId = 1L;
        long userIdBooker = 2L;

        User user = User.builder().id(userId).build();
        User user1 = User.builder().id(userIdBooker).build();
        Item item = Item.builder().id(itemId).owner(user).available(true).build();
        Booking booking = Booking.builder().item(item).build();

        when(itemRepository.findById(booking.getItem().getId())).thenReturn(Optional.of(item));

        when(userRepository.findById(userIdBooker)).thenReturn(Optional.of(user1));

        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking booking1 = bookingService.postBooking(userIdBooker, booking);

        assertNotNull(booking1);
    }

    @Test
    void approvedBookingTest() {
        long userId = 1L;
        long bookingId = 1L;
        long itemId = 1L;

        User user = User.builder().id(userId).build();
        Item item = Item.builder().id(itemId).owner(user).available(true).build();
        Booking booking = Booking.builder().status(Status.WAITING).item(item).build();

        when(bookingRepository.existsByItemOwnerIdOrBookerId(userId, userId)).thenReturn(true);

        when(bookingRepository.findByIdAndItemOwnerId(bookingId, userId)).thenReturn(Optional.of(booking));

        Booking booking1 = bookingService.approvedBooking(userId, bookingId, true);

        assertNotNull(booking1);
    }

    @Test
    void findBookingTest() {
        long userId = 1L;
        long bookingId = 1L;
        long itemId = 1L;

        User user = User.builder().id(userId).build();
        Item item = Item.builder().id(itemId).owner(user).available(true).build();
        BookingSearch booking = BookingSearch.builder().status(Status.WAITING).item(item).build();

        when(bookingRepository.findBooking(bookingId, userId, userId)).thenReturn(Optional.of(booking));

        BookingSearch booking1 = bookingService.findBooking(userId, bookingId);

        assertNotNull(booking1);
    }

    @Test
    void findListBookingTest() {
        long userId = 1L;
        long itemId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("start").descending());

        User user = User.builder().id(userId).build();
        Item item = Item.builder().id(itemId).owner(user).available(true).build();
        BookingSearch booking = BookingSearch.builder().item(item).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllByBookerIdAndStateCurrent(userId, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking = bookingService.findListBooking(userId, State.CURRENT, from, size);
        assertNotEmpty(listBooking, "не пуст");

        when(bookingRepository.findAllByBookerIdAndStatePast(userId, Status.APPROVED, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking1 = bookingService.findListBooking(userId, State.PAST, from, size);
        assertNotEmpty(listBooking1, "не пуст");

        when(bookingRepository.findAllByBookerIdAndStateFuture(userId, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking2 = bookingService.findListBooking(userId, State.FUTURE, from, size);
        assertNotEmpty(listBooking2, "не пуст");

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking3 = bookingService.findListBooking(userId, State.WAITING, from, size);
        assertNotEmpty(listBooking3, "не пуст");

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking4 = bookingService.findListBooking(userId, State.REJECTED, from, size);
        assertNotEmpty(listBooking4, "не пуст");

        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking5 = bookingService.findListBooking(userId, State.ALL, from, size);
        assertNotEmpty(listBooking5, "не пуст");

    }

    @Test
    void findListOwnerBookingTest() {
        long userId = 1L;
        long itemId = 1L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("start").descending());

        User user = User.builder().id(userId).build();
        Item item = Item.builder().id(itemId).owner(user).available(true).build();
        BookingSearch booking = BookingSearch.builder().item(item).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(bookingRepository.findAllByItemOwnerAndStateCurrent(userId, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking = bookingService.findListOwnerBooking(userId, State.CURRENT, from, size);
        assertNotEmpty(listBooking, "не пуст");

        when(bookingRepository.findAllByItemOwnerIdAndStatePast(userId, Status.APPROVED, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking1 = bookingService.findListOwnerBooking(userId, State.PAST, from, size);
        assertNotEmpty(listBooking1, "не пуст");

        when(bookingRepository.findAllByItemOwnerIdAndStateFuture(userId, Status.REJECTED, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking2 = bookingService.findListOwnerBooking(userId, State.FUTURE, from, size);
        assertNotEmpty(listBooking2, "не пуст");

        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking3 = bookingService.findListOwnerBooking(userId, State.WAITING, from, size);
        assertNotEmpty(listBooking3, "не пуст");

        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking4 = bookingService.findListOwnerBooking(userId, State.REJECTED, from, size);
        assertNotEmpty(listBooking4, "не пуст");

        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, pageable)).thenReturn(List.of(booking));
        List<BookingSearch> listBooking5 = bookingService.findListOwnerBooking(userId, State.ALL, from, size);
        assertNotEmpty(listBooking5, "не пуст");
    }
}
