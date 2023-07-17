package ru.practicum.shareit.gateway.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.client.BaseClient;
import ru.practicum.client.BookingClient;
import ru.practicum.dto.BookingDtoReceived;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingClientTest {

    @Mock
    private BaseClient baseClient;

    @InjectMocks
    private BookingClient bookingClient;

    private long userId = 1L;

    @Test
    void postBooking() {
//        BookingDtoReceived booking = BookingDtoReceived.builder().build();
//
//        ResponseEntity<Object> objectResponseEntity = bookingClient.postBooking(userId, booking);
//
//        assertEquals(objectResponseEntity, HttpStatus.OK);
    }

    @Test
    void approvedBooking() {
    }

    @Test
    void findBooking() {
    }

    @Test
    void findListBooking() {
    }

    @Test
    void findOwnerBooking() {
    }
}