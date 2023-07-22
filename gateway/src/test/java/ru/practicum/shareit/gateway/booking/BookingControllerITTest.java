package ru.practicum.shareit.gateway.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import ru.practicum.dto.State;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private long userId = 1L;

    private long itemId = 1L;

    private long bookingId = 1L;

    private BookingDtoReceived booking = BookingDtoReceived.builder()
            .itemId(itemId)
            .start(LocalDateTime.now().plusSeconds(1))
            .end(LocalDateTime.now().plusSeconds(10))
            .build();

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @SneakyThrows
    @Test
    void postBooking() {

        when(bookingClient.postBooking(userId, booking)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/bookings", booking)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId).content(objectMapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void postBookingNotValidStartAndEndAndUserID() {

        when(bookingClient.postBooking(userId, booking)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/bookings", booking)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", -1).content(objectMapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        booking.setStart(LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/bookings", booking)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId).content(objectMapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

        booking.setStart(LocalDateTime.now().plusSeconds(1));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/bookings", booking)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId).content(objectMapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());


    }

    @SneakyThrows
    @Test
    void approvedBooking() {
        boolean approved = true;

        when(bookingClient.approvedBooking(userId, bookingId, approved)).thenReturn(objectResponseEntity);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved))
                        .content(objectMapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void approvedBookingBadRequest() {
        boolean approved = true;

        when(bookingClient.approvedBooking(userId, bookingId, approved)).thenReturn(objectResponseEntity);

        mockMvc.perform(patch("/bookings/{bookingId}", -1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved))
                        .content(objectMapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void findBooking() {
        when(bookingClient.findBooking(userId, bookingId)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findBookingNotEntity() {
        when(bookingClient.findBooking(userId, bookingId)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/bookings/{bookingId}", -1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void findListBooking() {
        when(bookingClient.findListBooking(userId, State.ALL, 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", String.valueOf(State.ALL))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findListBookingNotValidUser() {
        when(bookingClient.findListBooking(-1, State.ALL, 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", -1)
                        .param("state", String.valueOf(State.ALL))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void findOwnerBooking() {
        when(bookingClient.findOwnerBooking(userId, State.ALL, 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", String.valueOf(State.ALL))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findOwnerBookingNotValidUser() {
        when(bookingClient.findOwnerBooking(-1, State.ALL, 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", -1)
                        .param("state", String.valueOf(State.ALL))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());
    }
}