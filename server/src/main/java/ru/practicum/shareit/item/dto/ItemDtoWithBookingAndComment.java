package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.SmallBooking;

import java.util.List;

@Data
@Builder
public class ItemDtoWithBookingAndComment {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private SmallBooking lastBooking;

    private SmallBooking nextBooking;

    private List<CommentDto> comments;

}
