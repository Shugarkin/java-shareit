package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto postBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody BookingDto booking) {
        Booking newBooking = bookingService.postBooking(userId, BookingMapper.toBooking(booking));
        return BookingMapper.toBookingDto(newBooking);
    }

    @PatchMapping
    public BookingDto approvedBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam boolean answer) {
        Booking newBooking = bookingService.approvedBooking(userId, answer);
    }

}
