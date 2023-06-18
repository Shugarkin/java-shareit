package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingApproved;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingService {

    Booking postBooking(Long userId, Booking booking);

    Booking approvedBooking(Long userId, Long bookingId, Boolean answer);

    BookingApproved findBooking(long userId, long bookingId);

    List<BookingApproved> findListBooking(long userId, Status state);

    List<BookingApproved> findListOwnerBooking(long userId, Status state);
}
