package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class Booking {

    @NotNull
    private Long id;


    private LocalDateTime start;

    @PastOrPresent
    private LocalDateTime finish;

    @NotNull
    private Long itemId;

    @NotNull
    private Long userId;

    @NotBlank
    private Status status;
}
