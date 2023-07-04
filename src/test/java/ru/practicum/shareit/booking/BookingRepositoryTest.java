package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Assertions;
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

    @Test
    void findBookingTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .item(item)
                .build();

        bookingRepository.save(booking);

        Optional<BookingSearch> booking1 = bookingRepository.findBooking(bookingId, userId, userId);

        Assertions.assertNotNull(booking1);

    }

    @Test
    void findAllByBookerIdAndStateCurrentTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(10))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStateCurrent = bookingRepository.findAllByBookerIdAndStateCurrent(userId2, pageable);

        assertNotEmpty(allByBookerIdAndStateCurrent, "не пустой");
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDescTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(10))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStateCurrent = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId2, Status.WAITING, pageable);

        assertNotEmpty(allByBookerIdAndStateCurrent, "не пустой");
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(10))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStateCurrent = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, pageable);

        assertNotEmpty(allByBookerIdAndStateCurrent, "не пустой");
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDescTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(10))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStateCurrent = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, pageable);

        assertNotEmpty(allByBookerIdAndStateCurrent, "не пустой");
    }

    @Test
    void findByIdAndItemOwnerIdTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(10))
                .item(item)
                .build();

        bookingRepository.save(booking);

        Optional<Booking> byIdAndItemOwnerId = bookingRepository.findByIdAndItemOwnerId(bookingId, userId);

        Assertions.assertNotNull(byIdAndItemOwnerId.get());
    }

    @Test
    void existsByItemOwnerIdOrBookerIdTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(10))
                .item(item)
                .build();

        bookingRepository.save(booking);

        boolean answer = bookingRepository.existsByItemOwnerIdOrBookerId(userId, userId2);

        Assertions.assertTrue(answer);
    }

    @Test
    void findAllByItemOwnerIdOrderByStartTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(10))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<Booking> allByItemOwnerIdOrderByStart = bookingRepository.findAllByItemOwnerIdOrderByStart(userId);

        assertNotEmpty(allByItemOwnerIdOrderByStart, "не пустой");
    }

    @Test
    void findAllByBookerIdOrderByStartDescTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.WAITING)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(10))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdOrderByStartDesc = bookingRepository.findAllByBookerIdOrderByStartDesc(userId2, pageable);

        assertNotEmpty(allByBookerIdOrderByStartDesc, "не пустой");
    }

    @Test
    void findAllByBookerIdAndStatePastTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStatePast = bookingRepository.findAllByBookerIdAndStatePast(userId2, Status.APPROVED, pageable);

        assertNotEmpty(allByBookerIdAndStatePast, "не пустой");
    }

    @Test
    void findAllByBookerIdAndStateFutureTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .booker(user1)
                .start(LocalDateTime.now().plusSeconds(10))
                .finish(LocalDateTime.now().plusSeconds(20))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByBookerIdAndStateFuture = bookingRepository.findAllByBookerIdAndStateFuture(userId2, pageable);

        assertNotEmpty(allByBookerIdAndStateFuture, "не пустой");
    }

    @Test
    void findAllByItemOwnerAndStateCurrentTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusSeconds(20))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByItemOwnerAndStateCurrent = bookingRepository.findAllByItemOwnerAndStateCurrent(userId, pageable);

        assertNotEmpty(allByItemOwnerAndStateCurrent, "не пустой");
    }

    @Test
    void findAllByItemOwnerIdAndStatePastTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByItemOwnerIdAndStatePast = bookingRepository.findAllByItemOwnerIdAndStatePast(userId, Status.APPROVED, pageable);

        assertNotEmpty(allByItemOwnerIdAndStatePast, "не пустой");
    }

    @Test
    void findAllByItemOwnerIdAndStateFutureTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .booker(user1)
                .start(LocalDateTime.now().plusSeconds(10))
                .finish(LocalDateTime.now().plusSeconds(20))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> allByItemOwnerIdAndStateFuture = bookingRepository.findAllByItemOwnerIdAndStateFuture(userId, Status.REJECTED, pageable);

        assertNotEmpty(allByItemOwnerIdAndStateFuture, "не пустой");
    }

    @Test
    void findAllByItemIdAndItemOwnerIdAndStatusOrderByStartTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .booker(user1)
                .start(LocalDateTime.now().plusSeconds(10))
                .finish(LocalDateTime.now().plusSeconds(20))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<Booking> allByItemIdAndItemOwnerIdAndStatusOrderByStart =
                bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(itemId, userId, Status.APPROVED);

        assertNotEmpty(allByItemIdAndItemOwnerIdAndStatusOrderByStart, "не пустой");
    }

    @Test
    void findFirstByItemIdAndBookerIdAndStatusAndFinishBeforeTest() {
        long userId = 1L;
        long userId2 = 2L;
        long itemId = 1L;
        long bookingId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();
        userRepository.save(user);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").owner(user).build();
        itemRepository.save(item);

        User user1 = User.builder().id(userId2).name("dgs").email("j@mail.com").build();
        userRepository.save(user1);

        Booking booking = Booking.builder()
                .id(bookingId)
                .status(Status.APPROVED)
                .booker(user1)
                .start(LocalDateTime.now())
                .finish(LocalDateTime.now().plusNanos(1))
                .item(item)
                .build();

        bookingRepository.save(booking);

        List<BookingSearch> firstByItemIdAndBookerIdAndStatusAndFinishBefore =
                bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(itemId, userId2, Status.APPROVED, pageable);

        assertNotEmpty(firstByItemIdAndBookerIdAndStatusAndFinishBefore, "не пустой");
    }
}
