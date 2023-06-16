package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {

    Booking postBooking(Long userId, Booking booking);

    Booking approvedBooking(Long userId, Boolean answer);
}
