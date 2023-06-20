package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.UselessBooking;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemDtoWithBooking {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private UselessBooking lastBooking;

    private UselessBooking nextBooking;

    private List<CommentDto> comments;

    public void addBooking(UselessBooking lastBookingNew, UselessBooking nextBookingNew) {
        lastBooking = lastBookingNew;
        nextBooking = nextBookingNew;
    }

    public void addComments(List<CommentDto> list) {
        if (list.isEmpty()) {
            comments = List.of();
        } else {
            comments = list;
        }
    }

}
