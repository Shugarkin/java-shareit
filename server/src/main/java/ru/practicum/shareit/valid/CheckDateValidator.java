package ru.practicum.shareit.valid;

import ru.practicum.shareit.booking.dto.BookingDtoReceived;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookingDtoReceived> {
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingDtoReceived bookingDtoReceived, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDtoReceived.getStart();
        LocalDateTime end = bookingDtoReceived.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}
