package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.UselessBooking;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemDtoWithBooking {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private UselessBooking nextBooking;

    private UselessBooking lastBooking;

}
