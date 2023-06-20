package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Column(name = "start_booking")
    private LocalDateTime start;

    @Column(name = "finish_booking")
    private LocalDateTime finish;

    @ManyToOne
    @JoinColumn(name = "items_id")
    private Item item;

    private Long itemId;


    @ManyToOne
    @JoinColumn(name = "users_id")
    private User booker;

    private Status status;

    public Booking() {
    }
}
