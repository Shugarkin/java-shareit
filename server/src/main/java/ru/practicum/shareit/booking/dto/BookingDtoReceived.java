package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.marker.Marker;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingDtoReceived {

    @NotNull(groups = {Marker.Create.class})
    @FutureOrPresent(groups = {Marker.Create.class})
    private LocalDateTime start;

    @NotNull(groups = {Marker.Create.class})
    @Future(groups = {Marker.Create.class})
    private LocalDateTime end;

    @NotNull(groups = {Marker.Create.class})
    private Long itemId;
}
