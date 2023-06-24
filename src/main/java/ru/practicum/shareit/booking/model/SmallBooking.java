package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SmallBooking {
    private Long id;

    private Long itemId;

    private Long bookerId;
}
