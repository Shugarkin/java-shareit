package ru.practicum.shareit.booking.dto;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingDtoReceived {

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;
}
