package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    long itemId = 1L;
    long userId = 1L;

    long userIdBooking = 1L;

    private User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();


    private User userBooking = User.builder().id(userIdBooking).name("dgsasd").email("f@mail.com").build();


    private Item item = Item.builder()
            .id(itemId)
            .name("asd")
            .available(true)
            .description("asdf").owner(user)
            .build();


    private Booking booking = Booking.builder().status(Status.APPROVED)
            .booker(userBooking)
            .item(item)
            .start(LocalDateTime.now())
            .finish(LocalDateTime.now().plusNanos(1))
            .build();


    private Comment comment = Comment.builder()
            .text("asdf")
            .item(item)
            .create(LocalDateTime.now())
            .user(userBooking)
            .build();

    @BeforeEach
    void before() {
        user = userRepository.save(user);
        userId = user.getId();

        userBooking = userRepository.save(userBooking);
        userIdBooking = userBooking.getId();

        item.setOwner(user);
        item = itemRepository.save(item);
        itemId = item.getId();

        booking.setBooker(userBooking);
        booking.setItem(item);
        booking = bookingRepository.save(booking);

        comment.setItem(item);
        comment.setUser(userBooking);
        comment = commentRepository.save(comment);
    }

    @AfterEach
    void after() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void findAllByUserId() {
        List<Comment> allByUserId = commentRepository.findAllByUserId(userIdBooking);

        assertEquals(1, allByUserId.size());
    }

    @Test
    void findAllByItemId() {
        List<Comment> allByItemId = commentRepository.findAllByItemId(itemId);

        assertEquals(1, allByItemId.size());
    }


}
