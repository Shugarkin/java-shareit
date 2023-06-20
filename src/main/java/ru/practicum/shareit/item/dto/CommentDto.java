package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 20, groups = {Marker.Create.class})
    private String text;

    private Long item;

    private String authorName;

    private LocalDateTime created;

}
