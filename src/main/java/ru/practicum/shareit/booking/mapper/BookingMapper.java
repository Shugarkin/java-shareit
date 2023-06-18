package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingApproved;
import ru.practicum.shareit.booking.model.UselessBooking;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .itemId(bookingDto.getItemId())
                .start(bookingDto.getStart())
                .finish(bookingDto.getEnd())
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getFinish())
                .status(booking.getStatus())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .build();
    }

    public List<BookingDto> toListBookingDto(List<Booking> list) {
        return list.stream().map(BookingMapper :: toBookingDto).collect(Collectors.toList());
    }

    public BookingDto appBookingToBookingDto(BookingApproved bookingApproved) {
        return BookingDto.builder()
                .id(bookingApproved.getId())
                .start(bookingApproved.getStart())
                .end(bookingApproved.getFinish())
                .status(bookingApproved.getStatus())
                .item(bookingApproved.getItem())
                .booker(bookingApproved.getBooker())
                .build();
    }

    public List<BookingDto> appBookingToDtoList(List<BookingApproved> list) {
        return list.stream().map(BookingMapper::appBookingToBookingDto).collect(Collectors.toList());
    }

    public UselessBooking toUseLess(Booking booking) {
        return UselessBooking.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public List<UselessBooking> toListUselessBooking(List<Booking> list) {
        return list.stream().map(BookingMapper::toUseLess).collect(Collectors.toList());
    }
}
