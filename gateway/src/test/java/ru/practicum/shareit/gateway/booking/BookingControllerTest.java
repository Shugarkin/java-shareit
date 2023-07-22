package ru.practicum.shareit.gateway.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.client.BookingClient;
import ru.practicum.controller.BookingController;
import ru.practicum.dto.BookingDtoReceived;
import ru.practicum.dto.State;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @Test
    void postBooking() {
        long userId = 1L;

        BookingDtoReceived bookingDtoReceived = BookingDtoReceived.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(10))
                .itemId(1L)
                .build();

        when(bookingClient.postBooking(userId, bookingDtoReceived)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> objectResponseEntity1 = bookingController.postBooking(userId, bookingDtoReceived);

        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    void approvedBooking() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;

        when(bookingClient.approvedBooking(userId, bookingId, approved)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> objectResponseEntity1 = bookingController.approvedBooking(userId, bookingId, approved);

        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    void findBooking() {
        long userId = 1L;
        long bookingId = 1L;

        when(bookingClient.findBooking(userId, bookingId)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> booking = bookingController.findBooking(userId, bookingId);

        assertEquals(objectResponseEntity, booking);
    }

    @Test
    void findListBooking() {
        long userId = 1L;

        when(bookingClient.findListBooking(userId, State.ALL, 0, 10)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> listBooking = bookingController.findListBooking(userId, State.ALL, 0, 10);

        assertEquals(objectResponseEntity, listBooking);
    }

    @Test
    void findOwnerBooking() {
        long userId = 1L;

        when(bookingClient.findOwnerBooking(userId, State.ALL, 0, 10)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> ownerBooking = bookingController.findOwnerBooking(userId, State.ALL, 0, 10);

        assertEquals(objectResponseEntity, ownerBooking);
    }
}

