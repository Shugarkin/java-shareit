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

    private User user;

    private User user1;

    private Item item;

    private Booking booking;

    @BeforeEach
    public void before() {
        user = userService.createUser(User.builder()
                .name("Викинг")
                .email("vikssssing@mail.com")
                .build());

        user1 = userService.createUser(User.builder()
                .name("Викинг")
                .email("ssssing@mail.com")
                .build());

        item = itemService.createItem(user.getId(), Item.builder().name("оружие")
                .available(true)
                .description("могучее")
                .build());

        booking = bookingService.postBooking(user1.getId(),  Booking.builder()
                .item(item)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .build());
    }

    @AfterEach
    public void after() {
        userService.deleteUser(user.getId());
        userService.deleteUser(user1.getId());
    }

    @Test
    public void test() {

        ItemWithBookingAndComment newItem = itemService.findItem(user.getId(), item.getId());
        Assertions.assertNotNull(newItem);

        List<ItemWithBookingAndComment> listItem = itemService.findAllItemByUser(user.getId());
        Assertions.assertNotNull(listItem);

        Item upItem = itemService.updateItem(user.getId(), item.getId(), Item.builder().name("огромное оружие").build());
        Assertions.assertNotNull(upItem);

        List<ItemSearch> newList = itemService.search(user.getId(), "оружие");
        Assertions.assertNotNull(newList);

        bookingService.approvedBooking(user.getId(), booking.getId(), true);

        Comment comment = itemService.createComment(user1.getId(), item.getId(), Comment.builder().text("все хорошо").build());
        Assertions.assertNotNull(comment);
    }
}
