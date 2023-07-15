package ru.practicum.shareit.gateway.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.client.BookingClient;
import ru.practicum.controller.BookingController;
import ru.practicum.dto.BookingDtoReceived;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;


    private long itemId = 1L;

    private long userId = 1L;

    private long bookingId = 1L;

    private LocalDateTime start;

    private LocalDateTime finish;

    private BookingDtoReceived booking;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @BeforeEach
    void before() {
        start = LocalDateTime.now().plusMinutes(1);
        finish = LocalDateTime.now().plusMinutes(10);

        booking = BookingDtoReceived.builder()
                .itemId(itemId)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(10))
                .build();
    }

    @SneakyThrows
    @Test
    void postBooking() {

        when(bookingClient.postBooking(userId, booking)).thenReturn(objectResponseEntity);


        mockMvc.perform(post("/bookings", booking)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());


        //assertEquals(bookingString, objectMapper.writeValueAsString(bookingDto));
    }

//    @SneakyThrows
//    @Test
//    void approvedBooking() {
//        boolean approved = true;
//
//        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
//
//        when(bookingService.approvedBooking(any(), any(), any())).thenReturn(booking);
//
//
//        String bookingString = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
//                        .contentType("application/json")
//                        .header("X-Sharer-User-Id", userId)
//                        .param("approved", String.valueOf(approved))
//                        .content(objectMapper.writeValueAsString(bookingDto))
//                        .characterEncoding(StandardCharsets.UTF_8))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals(bookingString, objectMapper.writeValueAsString(bookingDto));
//    }
//
//    @SneakyThrows
//    @Test
//    void findBooking() {
//
//        BookingSearch booking = BookingSearch.builder()
//                .item(item)
//                .status(Status.APPROVED)
//                .booker(user)
//                .start(LocalDateTime.now())
//                .finish(LocalDateTime.now().plusNanos(1))
//                .build();
//
//
//        when(bookingService.findBooking(userId, bookingId)).thenReturn(booking);
//
//        BookingDto bookingDto = BookingMapper.fromBookingSearchToBookingDto(booking);
//
//        String bookingString = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
//                        .contentType("application/json")
//                        .header("X-Sharer-User-Id", userId)
//                        .content(objectMapper.writeValueAsString(bookingDto))
//                        .characterEncoding(StandardCharsets.UTF_8))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertEquals(bookingString, objectMapper.writeValueAsString(bookingDto));
//    }
//
//    @SneakyThrows
//    @Test
//    void findListBooking() {
//        State state = State.ALL;
//        int from = 1;
//        int size = 10;
//
//        when(bookingService.findListBooking(userId, state, from, size)).thenReturn(List.of());
//
//        mockMvc.perform(get("/bookings")
//                        .header("X-Sharer-User-Id", userId)
//                        .param("state", String.valueOf(state))
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isOk());
//        verify(bookingService).findListBooking(userId, state, from, size);
//    }
//
//    @SneakyThrows
//    @Test
//    void findOwnerBooking() {
//        State state = State.ALL;
//        int from = 1;
//        int size = 10;
//
//        when(bookingService.findListOwnerBooking(userId, state, from, size)).thenReturn(List.of());
//
//        mockMvc.perform(get("/bookings/owner")
//                        .header("X-Sharer-User-Id", userId)
//                        .param("state", String.valueOf(state))
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isOk());
//        verify(bookingService).findListOwnerBooking(userId, state, from, size);
//    }
//

}

