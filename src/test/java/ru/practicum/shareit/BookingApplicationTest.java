package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingApplicationTest {

    private final ItemService itemService;

    private final UserService userService;

    private final BookingService bookingService;

    @Test
    public void test() {

        User user4 = userService.createUser(User.builder()
                .name("Викинг")
                .email("vikssss@mail.com")
                .build());

        User user5 = userService.createUser(User.builder()
                .name("Викинг")
                .email("ssing@mail.com")
                .build());

        Item item2 = itemService.createItem(1L, Item.builder().name("оруsssжие")
                .available(true)
                .description("могучее")
                .build());

        Booking booking2 = bookingService.postBooking(2L,  Booking.builder()
                .item(item2)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .build());
        Assertions.assertNotNull(booking2);

        Booking booking1 = bookingService.approvedBooking(1L, 1L, true);
        Assertions.assertNotNull(booking1);

        BookingSearch booking3 = bookingService.findBooking(1L, 1L);
        Assertions.assertNotNull(booking3);

        List<BookingSearch> list = bookingService.findListBooking(1L, State.ALL);
        Assertions.assertNotNull(list);

        List<BookingSearch> list1 = bookingService.findListOwnerBooking(2L, State.ALL);
        Assertions.assertNotNull(list1);
    }

}
