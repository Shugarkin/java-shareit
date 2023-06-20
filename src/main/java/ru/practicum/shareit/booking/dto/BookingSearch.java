package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingSearch {

    //класс нужен только для выгрузки данных из бд. наверно это лишнее, но я так и не понял почему hiber ругается на отсудтсвие конструктора
    private Long id;

    private LocalDateTime start;

    private LocalDateTime finish;

    private Status status;

    private Item item;

    private Long itemId;

    private User booker;

}
