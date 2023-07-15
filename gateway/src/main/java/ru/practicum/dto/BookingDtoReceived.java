package ru.practicum.dto;

import lombok.*;
import ru.practicum.valid.StartBeforeEndDateValid;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@StartBeforeEndDateValid
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
