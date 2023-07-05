package ru.practicum.shareit.request.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestSearch {
    private Long id;

    private String description;

    private LocalDateTime created;

}
