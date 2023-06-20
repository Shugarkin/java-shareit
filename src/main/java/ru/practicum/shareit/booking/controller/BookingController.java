package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto postBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                  @Validated(Marker.Create.class) @RequestBody BookingDto booking) {
        Booking newBooking = bookingService.postBooking(userId, BookingMapper.toBooking(booking));
        return BookingMapper.toBookingDto(newBooking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) final long userId,
                                      @PathVariable("bookingId") @Min(0) final long  bookingId,
                                      @RequestParam(required = false) boolean approved) {
        Booking newBooking = bookingService.approvedBooking(userId, bookingId, approved);
        return BookingMapper.toBookingDto(newBooking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) final long userId,
                                  @PathVariable("bookingId") @Min(0) final long bookingId) {
        BookingSearch newBooking = bookingService.findBooking(userId, bookingId);
        return BookingMapper.fromBookingSearchToBookingDto(newBooking);
    }

    @GetMapping
    public List<BookingDto> findListBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) final long userId,
                                            @RequestParam(required = false) Status state) {
        List<BookingSearch> listBooking = bookingService.findListBooking(userId, state);
        return BookingMapper.fromBookingSearchToDtoList(listBooking);
    }

    @GetMapping("/owner")
    public List<BookingDto> findOwnerBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) final long userId,
                                             @RequestParam(required = false) Status state) {
        List<BookingSearch> listBooking = bookingService.findListOwnerBooking(userId, state);
        return BookingMapper.fromBookingSearchToDtoList(listBooking);
    }

}
