package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.id = ?1 and (b.item.owner.id = ?2 or b.booker.id = ?3)")
    Optional<BookingSearch> findBooking(long bookingId, long userId, long userId1);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.finish > now() and b.start < now() ")
    List<BookingSearch> findAllByBookerIdAndStateCurrent(long userId, Pageable pageable);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.status = ?2 ")
    List<BookingSearch> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status state, Pageable pageable);


    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.status = ?2 ")
    List<BookingSearch> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status, Pageable pageable);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 ")
    List<BookingSearch> findAllByItemOwnerIdOrderByStartDesc(long userId, Pageable pageable);

    Optional<Booking> findByIdAndItemOwnerId(Long bookingId, Long userId);

    boolean existsByItemOwnerIdOrBookerId(Long userId, Long userId1);

    List<Booking> findAllByItemOwnerIdOrderByStart(Long userId);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1  ")
    List<BookingSearch> findAllByBookerIdOrderByStartDesc(long userId, Pageable pageable);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.status = ?2 and b.finish < now() ")
    List<BookingSearch> findAllByBookerIdAndStatePast(long userId, Status status, Pageable pageable);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and b.start > now() ")
    List<BookingSearch> findAllByBookerIdAndStateFuture(long userId, Pageable pageable);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.finish > now() and b.start < now() ")
    List<BookingSearch> findAllByItemOwnerAndStateCurrent(long userId, Pageable pageable);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.finish < now() and b.status = ?2 ")
    List<BookingSearch> findAllByItemOwnerIdAndStatePast(long userId, Status status, Pageable pageable);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.owner.id = ?1 and b.start > now() and not b.status =?2 ")
    List<BookingSearch> findAllByItemOwnerIdAndStateFuture(long userId, Status status, Pageable pageable);

    List<Booking> findAllByItemIdAndItemOwnerIdAndStatusOrderByStart(Long itemId, Long userId, Status status);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.item.id = ?1 and b.booker.id = ?2 and b.status = ?3 and b.finish < now() " +
            "order by b.start desc " )
    List<BookingSearch> findFirstByItemIdAndBookerIdAndStatusAndFinishBefore(Long itemId, Long userId,
                                                                                 Status status, Pageable pageable);

}
