package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentReceiving {

        private Long id;

        private String text;

        private Long item;

        private String authorName;

        private LocalDateTime created;
}
