package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto postBooking(@RequestHeader("X-Sharer-User-Id")  long userId, @RequestBody BookingDtoReceived booking) {
        Booking newBooking = bookingService.postBooking(userId, BookingMapper.fromBookingDtoReceivedToBooking(booking));
        return BookingMapper.toBookingDto(newBooking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedBooking(@RequestHeader("X-Sharer-User-Id") final long userId,
                                      @PathVariable("bookingId") final long  bookingId,
                                      @RequestParam boolean approved) {
        Booking newBooking = bookingService.approvedBooking(userId, bookingId, approved);
        return BookingMapper.toBookingDto(newBooking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBooking(@RequestHeader("X-Sharer-User-Id") final long userId,
                                  @PathVariable("bookingId") final long bookingId) {
        BookingSearch newBooking = bookingService.findBooking(userId, bookingId);
        return BookingMapper.fromBookingSearchToBookingDto(newBooking);
    }

    @GetMapping
    public List<BookingDto> findListBooking(@RequestHeader("X-Sharer-User-Id")  final long userId,
                                            @RequestParam(defaultValue = "ALL") State state,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        List<BookingSearch> listBooking = bookingService.findListBooking(userId, state, from, size);
        return BookingMapper.fromBookingSearchToDtoList(listBooking);
    }

    @GetMapping("/owner")
    public List<BookingDto> findOwnerBooking(@RequestHeader("X-Sharer-User-Id")  final long userId,
                                             @RequestParam(defaultValue = "ALL") State state,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        List<BookingSearch> listBooking = bookingService.findListOwnerBooking(userId, state, from, size);
        return BookingMapper.fromBookingSearchToDtoList(listBooking);
    }

}
