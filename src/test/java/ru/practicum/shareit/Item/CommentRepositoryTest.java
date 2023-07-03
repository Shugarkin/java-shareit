package ru.practicum.shareit.Item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    //тоже самое, что и с тестами itemRepository

    @Test
    void findAllByUserIdTest() {
        long itemId = 6L;
        long userId = 6L;

        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        userRepository.save(user);

        User userBooking = User.builder().id(userId).name("dgsasd").email("f@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId)
                .name("asd")
                .available(true)
                .description("asdf").owner(user)
                .build();
        itemRepository.save(item);

        Booking booking = Booking.builder().status(Status.APPROVED)
                .booker(userBooking)
                .item(item)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .build();
        bookingRepository.save(booking);

        Comment comment = Comment.builder()
                .text("asdf")
                .item(item)
                .create(LocalDateTime.now())
                .user(userBooking)
                .build();
        commentRepository.save(comment);

        List<Comment> allByUserId = commentRepository.findAllByUserId(userId);

        assertEquals(1, allByUserId.size());
    }

    @Test
    void findAllByItemId() {
        long itemId = 5L;
        long userId = 5L;

        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        userRepository.save(user);

        User userBooking = User.builder().id(userId).name("dgsasd").email("f@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId)
                .name("asd")
                .available(true)
                .description("asdf").owner(user)
                .build();
        itemRepository.save(item);

        Booking booking = Booking.builder().status(Status.APPROVED)
                .booker(userBooking)
                .item(item)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .build();
        bookingRepository.save(booking);

        Comment comment = Comment.builder()
                .text("asdf")
                .item(item)
                .create(LocalDateTime.now())
                .user(userBooking)
                .build();
        commentRepository.save(comment);

        List<Comment> allByItemId = commentRepository.findAllByItemId(itemId);

        assertEquals(1, allByItemId.size());
    }


}
