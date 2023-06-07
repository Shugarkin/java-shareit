package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDto {

    private Long id;

    @NotBlank(groups = {Marker.Create.class})
    private String name;

    @NotEmpty(groups = {Marker.Create.class})
    @Email
    private String email;
}
