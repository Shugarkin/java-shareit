package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void postBookingTest() {
        long userId = 1L;
        long itemId = 1L;
        Item item = Item.builder().id(itemId).build();
        User user = User.builder().id(userId).build();
        Booking booking = Booking.builder().item(item).build();
        BookingDtoReceived bookingDtoReceived = BookingDtoReceived.builder().itemId(itemId).build();
        Booking booking1 = BookingMapper.fromBookingDtoReceivedToBooking(bookingDtoReceived);
        booking1.setBooker(user);

        when(bookingService.postBooking(userId, booking)).thenReturn(booking1);

        BookingDto newBooking = bookingController.postBooking(userId, bookingDtoReceived);
        BookingDto newBookingDto = BookingMapper.toBookingDto(booking1);

        assertEquals(newBooking, newBookingDto);
    }

    @Test
    void approvedBookingTest() {
        long userId = 1L;
        long itemId = 1L;
        long bookingId = 1L;
        boolean approved = true;
        User user = User.builder().id(userId).build();
        Item item = Item.builder().id(itemId).owner(user).available(true).build();
        Booking booking = Booking.builder().item(item).booker(user).status(Status.APPROVED).build();

        when(bookingService.approvedBooking(userId, bookingId, approved)).thenReturn(booking);

        BookingDto bookingDto = bookingController.approvedBooking(userId, bookingId, approved);
        BookingDto bookingDto1 = BookingMapper.toBookingDto(booking);

        assertEquals(bookingDto, bookingDto1);
    }

    @Test
    void findBookingTest() {
        long userId = 1L;
        long itemId = 1L;
        long bookingId = 1L;
        Item item = Item.builder().id(itemId).build();
        User user = User.builder().id(userId).build();
        BookingSearch booking = BookingSearch.builder().item(item).booker(user).build();

        when(bookingService.findBooking(userId, bookingId)).thenReturn(booking);

        BookingDto booking1 = bookingController.findBooking(userId, bookingId);
        BookingDto bookingDto = BookingMapper.fromBookingSearchToBookingDto(booking);

        assertEquals(booking1, bookingDto);
    }

    @Test
    void findListBookingTest() {
        long userId = 1L;

        when(bookingService.findListBooking(userId, State.ALL, 0, 10)).thenReturn(List.of());

        List<BookingDto> listBooking = bookingController.findListBooking(userId, State.ALL, 0, 10);
        List<BookingDto> listBooking1 = List.of();

        assertEquals(listBooking, listBooking1);
    }

    @Test
    void findOwnerBookingTest() {
        long userId = 1L;

        when(bookingService.findListOwnerBooking(userId, State.ALL, 0, 10)).thenReturn(List.of());

        List<BookingDto> listBooking = bookingController.findOwnerBooking(userId, State.ALL, 0, 10);
        List<BookingDto> listBooking1 = List.of();

        assertEquals(listBooking, listBooking1);
    }
}
