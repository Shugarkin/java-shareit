package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.marker.Marker;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private Long id;

    @NotNull(groups = {Marker.Create.class})
    @FutureOrPresent(groups = {Marker.Create.class})
    private LocalDateTime start;

    @NotNull(groups = {Marker.Create.class})
    @FutureOrPresent(groups = {Marker.Create.class})
    private LocalDateTime end;

    @NotNull(groups = {Marker.Create.class})
    private Long itemId;

    private Item item;

    private User booker;

    private Status status;
}
