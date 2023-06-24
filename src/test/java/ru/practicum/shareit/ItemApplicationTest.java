package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingAndComment;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemApplicationTest {

    private final ItemService itemService;

    private final UserService userService;

    private final BookingService bookingService;

    @BeforeEach
    public void before() {
        User user2 = userService.createUser(User.builder()
                .name("Викинг")
                .email("vikssssing@mail.com")
                .build());

        User user3 = userService.createUser(User.builder()
                .name("Викинг")
                .email("ssssing@mail.com")
                .build());

        Item item = itemService.createItem(2L, Item.builder().name("оружие")
                .available(true)
                .description("могучее")
                .build());

        Booking booking = bookingService.postBooking(1L,  Booking.builder()
                .item(item)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .build());
    }

    @AfterEach
    public void after() {
        userService.deleteUser(1L);
        userService.deleteUser(2L);
    }

    @Test
    public void test() {

        ItemWithBookingAndComment newItem = itemService.findItem(2L, 1L);
        Assertions.assertNotNull(newItem);

        List<ItemWithBookingAndComment> listItem = itemService.findAllItemByUser(2L);
        Assertions.assertNotNull(listItem);

        Item upItem = itemService.updateItem(2L, 1L, Item.builder().name("огромное оружие").build());
        Assertions.assertNotNull(upItem);

        List<ItemSearch> newList = itemService.search(1L, "оружие");
        Assertions.assertNotNull(newList);

        bookingService.approvedBooking(2L, 1L, true);

        Comment comment = itemService.createComment(1L, 1L, Comment.builder().text("все хорошо").build());
        Assertions.assertNotNull(comment);
    }
}
