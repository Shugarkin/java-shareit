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

    public void addBooking(List<UselessBooking> list) {
        if (list.isEmpty()) {
            lastBooking = null;
            nextBooking = null;
        } else if (list.size() == 1) {
            lastBooking = new UselessBooking(0L,0L,0L);
            lastBooking.setId(list.get(0).getId());
            lastBooking.setBookerId(list.get(0).getBookerId());
        } else {
            lastBooking = new UselessBooking(0L,0L,0L);
            nextBooking = new UselessBooking(0L,0L,0L);
            lastBooking.setId(list.get(0).getId());
            lastBooking.setBookerId(list.get(0).getBookerId());
            nextBooking.setId(list.get(1).getId());
            nextBooking.setBookerId(list.get(1).getBookerId());
        }
    }

    public void addComments(List<CommentDto> list) {
        if (list.isEmpty()) {
            comments = List.of();
        } else {
            comments.addAll(list);
        }
    }

}
