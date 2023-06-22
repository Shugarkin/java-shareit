package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingDtoItem item;

    private BookingDtoBooker booker;

    private Status status;

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class BookingDtoItem {
        private Long id;
        private String name;
    }


    @AllArgsConstructor
    @Getter
    @Setter
    public static class BookingDtoBooker {
        private Long id;
    }
}
