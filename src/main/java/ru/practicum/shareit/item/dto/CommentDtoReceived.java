package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDtoReceived {

    @NotBlank(groups = {Marker.Create.class})
    @Size(min = 1, max = 20, groups = {Marker.Create.class})
    private String text;


}
