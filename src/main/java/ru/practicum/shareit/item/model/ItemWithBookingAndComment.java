package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.SmallBooking;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemWithBookingAndComment {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private SmallBooking lastBooking;

    private SmallBooking nextBooking;

    private List<CommentDto> comments;

    public void addBooking(SmallBooking lastBookingNew, SmallBooking nextBookingNew) {
        if (lastBookingNew == null) {
            lastBooking = nextBookingNew;
            nextBooking = null;
        } else {
            lastBooking = lastBookingNew;
            nextBooking = nextBookingNew;
        }
    }

    public void addComments(List<CommentDto> list) {
        if (list.isEmpty()) {
            comments = List.of();
        } else {
            comments = list;
        }
    }

}