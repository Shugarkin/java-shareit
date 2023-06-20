package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.id = ?1 and (b.item.owner.id = ?2 or b.booker.id = ?3)")
    Optional<BookingSearch> findBooking(long bookingId, long userId, long userId1);

    List<BookingSearch> findAllByBookerIdOrderByStartDesc(long userId);

    List<BookingSearch> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status state);

    @Query("select new ru.practicum.shareit.booking.dto.BookingSearch(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking  as b " +
            "where b.item.owner.id = ?1 and (b.status = ?2 or b.status = ?3) " +
            "order by b.start desc ")
    List<BookingSearch> findAllByItemOwnerIdAndStatus(long userId, Status status, Status status1);

    List<BookingSearch> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status);

    List<BookingSearch> findAllByItemOwnerIdOrderByStartDesc(long userId);

    Optional<Booking> findByIdAndItemOwnerId(Long bookingId, Long userId);

    boolean existsByItemOwnerIdOrBookerId(Long userId, Long userId1);

    List<Booking> findAllByItemIdAndItemOwnerIdAndStatusNotOrderByStart(Long itemId, Long userId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStart(Long userId);

    Optional<Booking> findFirstByItemIdAndBookerIdAndStatus(Long itemId, Long userId, Status status);

}
