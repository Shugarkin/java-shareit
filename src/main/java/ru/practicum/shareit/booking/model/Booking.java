package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

}
