package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private Long id;

    @NotNull(groups = {Marker.Create.class})
    @FutureOrPresent(groups = {Marker.Create.class})
    private LocalDateTime start;

    @NotNull(groups = {Marker.Create.class})
    @FutureOrPresent(groups = {Marker.Create.class})
    private LocalDateTime end;

    @NotNull(groups = {Marker.Create.class})
    private Long itemId;

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
