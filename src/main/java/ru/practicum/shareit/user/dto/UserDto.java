package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {

    private Long id;

    @NotBlank(groups = {Marker.Create.class})
    @Size(min = 1, max = 10, groups = {Marker.Create.class, Marker.Update.class})
    private String name;

    @NotEmpty(groups = {Marker.Create.class})
    @Email(groups = {Marker.Create.class, Marker.Update.class})
    @Size(min = 1, max = 20, groups = {Marker.Create.class, Marker.Update.class})
    private String email;
}
