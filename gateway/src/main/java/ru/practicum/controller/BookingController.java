package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.BookingClient;
import ru.practicum.dto.BookingDtoReceived;
import ru.practicum.dto.State;
import ru.practicum.dto.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> postBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) long userId,
                                              @Validated(Marker.Create.class) @RequestBody @Valid BookingDtoReceived booking) {
        return bookingClient.postBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approvedBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) final long userId,
                                      @PathVariable("bookingId") @Min(0) final long  bookingId,
                                      @RequestParam boolean approved) {

        return ;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) final long userId,
                                  @PathVariable("bookingId") @Min(0) final long bookingId) {
        BookingSearch newBooking = bookingService.findBooking(userId, bookingId);
        return BookingMapper.fromBookingSearchToBookingDto(newBooking);
    }

    @GetMapping
    public List<ResponseEntity<Object>> findListBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) final long userId,
                                            @RequestParam(defaultValue = "ALL") State state,
                                            @RequestParam(defaultValue = "0") @Min(0)  int from,
                                            @RequestParam(defaultValue = "10") @Min(1)  int size) {
        List<BookingSearch> listBooking = bookingService.findListBooking(userId, state, from, size);
        return BookingMapper.fromBookingSearchToDtoList(listBooking);
    }

    @GetMapping("/owner")
    public List<ResponseEntity<Object>> findOwnerBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) final long userId,
                                             @RequestParam(defaultValue = "ALL") State state,
                                             @RequestParam(defaultValue = "0") @Min(0)  int from,
                                             @RequestParam(defaultValue = "10") @Min(1)  int size) {
        List<BookingSearch> listBooking = bookingService.findListOwnerBooking(userId, state, from, size);
        return BookingMapper.fromBookingSearchToDtoList(listBooking);
    }

}
