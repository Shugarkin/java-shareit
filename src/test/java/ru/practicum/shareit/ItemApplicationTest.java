package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComment;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemApplicationTest {

    private final ItemService itemService;

    private final UserService userService;

    private final BookingService bookingService;
    @Test
    public void test() {
        User user = userService.createUser(User.builder()
                .name("Викинг")
                .email("vikssssing@mail.com")
                .build());

        Booking booking = bookingService.postBooking(1L, 2L);

        Item item = itemService.createItem(1L, Item.builder().name("оружие")
                .available(true)
                .description("могучее")
                .build());
        Assertions.assertNotNull(item);

        ItemDtoWithBookingAndComment newItem = itemService.findItem(2L, 1L);
        Assertions.assertNotNull(newItem);

        List<ItemDtoWithBookingAndComment> listItem = itemService.findAllItemByUser(2L);
        Assertions.assertNotNull(listItem);

        Item upItem = itemService.updateItem(2L, 1L, Item.builder().name("огромное оружие").build());
        Assertions.assertNotNull(upItem);

        List<ItemSearch> newList = itemService.search(2L, "оружие");
        Assertions.assertNotNull(newList);
    }
}
