package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoReceived;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.SmallBooking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        BookingDto.BookingDtoItem item = new BookingDto.BookingDtoItem(booking.getItem().getId(), booking.getItem().getName());
        BookingDto.BookingDtoBooker booker = new BookingDto.BookingDtoBooker(booking.getBooker().getId());
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getFinish())
                .status(booking.getStatus())
                .item(item)
                .booker(booker)
                .build();
    }

    public BookingDto fromBookingSearchToBookingDto(BookingSearch bookingApproved) {
        BookingDto.BookingDtoItem item = new BookingDto.BookingDtoItem(bookingApproved.getItem().getId(), bookingApproved.getItem().getName());
        BookingDto.BookingDtoBooker booker = new BookingDto.BookingDtoBooker(bookingApproved.getBooker().getId());
        return BookingDto.builder()
                .id(bookingApproved.getId())
                .start(bookingApproved.getStart())
                .end(bookingApproved.getFinish())
                .status(bookingApproved.getStatus())
                .item(item)
                .booker(booker)
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

    public Booking fromBookingDtoReceivedToBooking(BookingDtoReceived bookingDtoReceived) {
        return Booking.builder()
                .item(Item.builder().id(bookingDtoReceived.getItemId()).build())
                .start(bookingDtoReceived.getStart())
                .finish(bookingDtoReceived.getEnd())
                .build();
    }
}
