package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.SmallBooking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .item(Item.builder().id(bookingDto.getItemId()).build())
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

    public BookingDto fromBookingSearchToBookingDto(BookingSearch bookingApproved) {
        return BookingDto.builder()
                .id(bookingApproved.getId())
                .start(bookingApproved.getStart())
                .end(bookingApproved.getFinish())
                .status(bookingApproved.getStatus())
                .item(bookingApproved.getItem())
                .booker(bookingApproved.getBooker())
                .build();
    }

    public List<BookingDto> fromBookingSearchToDtoList(List<BookingSearch> list) {
        return list.stream().map(BookingMapper::fromBookingSearchToBookingDto).collect(Collectors.toList());
    }

    public SmallBooking toSmallBooking(Booking booking) {
        return SmallBooking.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
