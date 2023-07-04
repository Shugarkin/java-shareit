package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingSearch {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime finish;

    private Status status;

    private Item item;

    private Long itemId;

    private User booker;

}
