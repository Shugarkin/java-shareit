package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemSearch {
    //тоже самое, класс тольео для выгрузки из бд

    private Long id;

    private String name;

    private String description;

    private Boolean available;

}
