package ru.practicum.shareit.booking.model;

import jdk.jshell.spi.ExecutionControl;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityBooking {
    private UselessBooking lastBooking;
    private UselessBooking nextBooking;
}
