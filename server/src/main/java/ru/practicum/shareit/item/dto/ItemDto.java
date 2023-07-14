package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class ItemDto {

    private Long id;

    @NotBlank(groups = Marker.Create.class)
    @Size(min = 1, max = 20, groups = {Marker.Create.class, Marker.Update.class})
    private String name;

    @NotBlank(groups = Marker.Create.class)
    @Size(min = 1, max = 40, groups = {Marker.Create.class, Marker.Update.class})
    private String description;

    @NotNull(groups = Marker.Create.class)
    private Boolean available;

    private Long requestId;
}
