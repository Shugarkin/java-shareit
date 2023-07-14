package ru.practicum.shareit.booking;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private long userId = 1L;
    private long userId2 = 2L;
    private long itemId = 1L;
    private long bookingId = 1L;

    private User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();

    private Item item = Item.builder()
            .id(itemId).name("asd").available(true).description("asdf").owner(user).build();

    private User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();

    private Booking booking = Booking.builder()
            .id(bookingId)
            .status(Status.WAITING)
            .booker(user1)
            .start(LocalDateTime.now())
            .finish(LocalDateTime.now().plusNanos(1))
            .item(item)
            .build();

    private Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void before() {
        //это все для того чтобы обойти присвоение id самой бд и не писать кучу однотипного кода,
        // а так же чтобы не угадывать каждый раз что присвоило бд в других классах
        user = userRepository.save(user);
        userId = user.getId();

        item.setOwner(user);
        item = itemRepository.save(item);
        itemId = item.getId();

        user1 = userRepository.save(user1);
        userId2 = user1.getId();

        booking.setItem(item);
        booking.setBooker(user1);
        booking = bookingRepository.save(booking);
        bookingId = booking.getId();
    }

    @AfterEach
    void after() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void findBooking() {

        Optional<BookingSearch> booking1 = bookingRepository.findBooking(bookingId, userId, userId);

        Assertions.assertNotNull(booking1);

    }

    @Test
    void findAllByBookerIdAndStateCurrent() {
        booking.setFinish(LocalDateTime.now().plusSeconds(10));

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStateCurrent = bookingRepository.findAllByBookerIdAndStateCurrent(userId2, pageable);

        assertNotEmpty(allByBookerIdAndStateCurrent, "не пустой");
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {

        List<BookingSearch> allByBookerIdAndStateCurrent = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId2, Status.WAITING, pageable);

        assertNotEmpty(allByBookerIdAndStateCurrent, "не пустой");
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {

        List<BookingSearch> allByBookerIdAndStateCurrent = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable);

        assertNotEmpty(allByBookerIdAndStateCurrent, "не пустой");
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDesc() {


        List<BookingSearch> allByBookerIdAndStateCurrent = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, pageable);

        assertNotEmpty(allByBookerIdAndStateCurrent, "не пустой");
    }

    @Test
    void findByIdAndItemOwnerId() {


        Optional<Booking> byIdAndItemOwnerId = bookingRepository.findByIdAndItemOwnerId(bookingId, userId);

        Assertions.assertNotNull(byIdAndItemOwnerId.get());
    }

    @Test
    void existsByItemOwnerIdOrBookerId() {

        boolean answer = bookingRepository.existsByItemOwnerIdOrBookerId(userId, userId2);

        Assertions.assertTrue(answer);
    }

    @Test
    void findAllByItemOwnerIdOrderByStart() {

        List<Booking> allByItemOwnerIdOrderByStart = bookingRepository.findAllByItemOwnerIdOrderByStart(userId);

        assertNotEmpty(allByItemOwnerIdOrderByStart, "не пустой");
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {

        List<BookingSearch> allByBookerIdOrderByStartDesc = bookingRepository.findAllByBookerIdOrderByStartDesc(userId2, pageable);

        assertNotEmpty(allByBookerIdOrderByStartDesc, "не пустой");
    }

    @Test
    void findAllByBookerIdAndStatePast() {
        booking.setStatus(Status.APPROVED);
        booking.setFinish(LocalDateTime.now().minusNanos(1));

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStatePast = bookingRepository.findAllByBookerIdAndStatePast(userId2, Status.APPROVED, pageable);

        assertNotEmpty(allByBookerIdAndStatePast, "не пустой");
    }

    @Test
    void findAllByBookerIdAndStateFuture() {
        booking.setStart(LocalDateTime.now().plusSeconds(10));
        booking.setFinish(LocalDateTime.now().plusSeconds(20));

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStateFuture = bookingRepository.findAllByBookerIdAndStateFuture(userId2, pageable);

        assertNotEmpty(allByBookerIdAndStateFuture, "не пустой");
    }

    @Test
    void findAllByItemOwnerAndStateCurrent() {
        booking.setFinish(LocalDateTime.now().plusSeconds(20));

        bookingRepository.save(booking);

        List<BookingSearch> allByItemOwnerAndStateCurrent = bookingRepository.findAllByItemOwnerAndStateCurrent(userId, pageable);

        assertNotEmpty(allByItemOwnerAndStateCurrent, "не пустой");
    }

    @Test
    void findAllByItemOwnerIdAndStatePast() {
        booking.setStatus(Status.APPROVED);
        booking.setFinish(LocalDateTime.now());

        bookingRepository.save(booking);


        List<BookingSearch> allByItemOwnerIdAndStatePast = bookingRepository.findAllByItemOwnerIdAndStatePast(userId, Status.APPROVED, pageable);

        assertNotEmpty(allByItemOwnerIdAndStatePast, "не пустой");
    }

    @Test
    void findAllByItemOwnerIdAndStateFuture() {
        booking.setStatus(Status.APPROVED);
        booking.setStart(LocalDateTime.now().plusSeconds(10));
        booking.setFinish(LocalDateTime.now().plusSeconds(20));

        bookingRepository.save(booking);

        List<BookingSearch> allByItemOwnerIdAndStateFuture = bookingRepository.findAllByItemOwnerIdAndStateFuture(userId, Status.REJECTED, pageable);

        assertNotEmpty(allByItemOwnerIdAndStateFuture, "не пустой");
    }

    @Test
    void findAllByItemIdAndItemOwnerIdAndStatusOrderByStart() {
        booking.setStatus(Status.APPROVED);
        booking.setStart(LocalDateTime.now().plusSeconds(10));
        booking.setFinish(LocalDateTime.now().plusSeconds(20));

        bookingRepository.save(booking);

        List<Booking> allByItemIdAndItemOwnerIdAndStatusOrderByStart =
                bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(itemId, userId, Status.APPROVED);

        assertNotEmpty(allByItemIdAndItemOwnerIdAndStatusOrderByStart, "не пустой");
    }

    @Test
    void findFirstByItemIdAndBookerIdAndStatusAndFinishBefore() {
        booking.setStatus(Status.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setFinish(LocalDateTime.now().plusNanos(1));

        bookingRepository.save(booking);

        List<BookingSearch> firstByItemIdAndBookerIdAndStatusAndFinishBefore =
                bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(itemId, userId2, Status.APPROVED, pageable);

        assertNotEmpty(firstByItemIdAndBookerIdAndStatusAndFinishBefore, "не пустой");
    }
}
