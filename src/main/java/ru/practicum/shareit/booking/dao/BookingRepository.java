package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.id = ?1 and (b.item.owner.id = ?2 or b.booker.id = ?3)")
    Optional<BookingSearch> findBooking(long bookingId, long userId, long userId1);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.finish > now() and b.start < now() " +
            "order by b.start desc ")
    List<BookingSearch> findAllByBookerIdAndStateCurrent(long userId);

    List<BookingSearch> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status state);


    List<BookingSearch> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status);

    List<BookingSearch> findAllByItemOwnerIdOrderByStartDesc(long userId);

    Optional<Booking> findByIdAndItemOwnerId(Long bookingId, Long userId);

    boolean existsByItemOwnerIdOrBookerId(Long userId, Long userId1);

    List<Booking> findAllByItemOwnerIdOrderByStart(Long userId);

//    @Query(value = "select * " +
//            "from booking as b join items as it on b.items_id = it.item_id " +
//            "join users as u on b.users_id = u.user_id " +
//            "where b.items_id = ?1 and b.users_id = ?2 and b.status = ?3 and b.finish < now() " +
//            "limit 1", nativeQuery = true)
//    Optional<BookingSearch> findFirstByItemIdAndBookerIdAndStatus(Long itemId, Long userId, Status status);

    List<BookingSearch> findAllByBookerIdOrderByStartDesc(long userId);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.status = ?2 and b.finish < now() " +
            "order by b.start desc ")
    List<BookingSearch> findAllByBookerIdAndStatePast(long userId, Status status);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.start > now() " +
            "order by b.start desc ")
    List<BookingSearch> findAllByBookerIdAndStateFuture(long userId);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.finish > now() and b.start < now() " +
            "order by b.start desc ")
    List<BookingSearch> findAllByItemOwnerAndStateCurrent(long userId);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.finish < now() and b.status =?2 " +
            "order by b.start desc ")
    List<BookingSearch> findAllByItemOwnerIdAndStatePast(long userId, Status status);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.start > now() and not b.status =?2 " +
            "order by b.start desc ")
    List<BookingSearch> findAllByItemOwnerIdAndStateFuture(long userId, Status status);

    List<Booking> findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(Long itemId, Long userId, Status status);

    Optional<BookingSearch> findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(Long itemId, Long userId, Status status, LocalDateTime timeNow);
}
