package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
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
    private BookingService bookingService;

    private User user;

    private Item item;

    private long itemId = 1L;

    private long userId = 1L;

    private long bookingId = 1L;

    private LocalDateTime start;

    private LocalDateTime finish;

    private Booking booking;

    @BeforeEach
    void before() {
        user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();

        item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").build();


        start = LocalDateTime.now().plusMinutes(1);
        finish = LocalDateTime.now().plusMinutes(10);

        booking = Booking.builder()
                .item(item)
                .status(Status.APPROVED)
                .booker(user)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .build();
    }

    @SneakyThrows
    @Test
    void postBooking() {
        BookingDtoReceived bookingDtoReceived = BookingDtoReceived.builder()
                .itemId(itemId)
                .start(start)
                .end(finish)
                .build();

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        when(bookingService.postBooking(any(), any())).thenReturn(booking);


        String bookingString = mockMvc.perform(post("/bookings", bookingDtoReceived)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingDtoReceived))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(bookingString, objectMapper.writeValueAsString(bookingDto));
    }

    @SneakyThrows
    @Test
    void approvedBooking() {
        boolean approved = true;

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        when(bookingService.approvedBooking(any(), any(), any())).thenReturn(booking);


        String bookingString = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved))
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(bookingString, objectMapper.writeValueAsString(bookingDto));
    }

    @SneakyThrows
    @Test
    void findBooking() {

        BookingSearch booking = BookingSearch.builder()
                .item(item)
                .status(Status.APPROVED)
                .booker(user)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .build();


        when(bookingService.findBooking(userId, bookingId)).thenReturn(booking);

        BookingDto bookingDto = BookingMapper.fromBookingSearchToBookingDto(booking);

        String bookingString = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(bookingString, objectMapper.writeValueAsString(bookingDto));
    }

    @SneakyThrows
    @Test
    void findListBooking() {
        State state = State.ALL;
        int from = 1;
        int size = 10;

        when(bookingService.findListBooking(userId, state, from, size)).thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("State", String.valueOf(state))
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
        verify(bookingService).findListBooking(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void findOwnerBooking() {
        State state = State.ALL;
        int from = 1;
        int size = 10;

        when(bookingService.findListOwnerBooking(userId, state, from, size)).thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("State", String.valueOf(state))
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
        verify(bookingService).findListOwnerBooking(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void postBookingBadRequest() {
        BookingDtoReceived bookingDtoReceived = BookingDtoReceived.builder()
                .start(start)
                .end(finish)
                .build();

        when(bookingService.postBooking(any(), any())).thenReturn(booking);

        mockMvc.perform(post("/bookings", bookingDtoReceived)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingDtoReceived))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
