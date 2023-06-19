package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingApproved;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

//    @Query("select new ru.practicum.shareit.booking.dto.BookingApproved(b.id, b.start, b.finish, b.status, b.item, b.booker) " +
//            "from Booking as b " +
//            "where b.item.owner.id = ?1 and b.id = ?2 and b.status = ?3")
//    Optional<Booking> findByIdAndItemOwnerId(Long userId, Long bookingId, Status status);


    @Query("select new ru.practicum.shareit.booking.dto.BookingApproved(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.id = ?1 and (b.item.owner.id = ?2 or b.booker.id = ?3)")
    Optional<BookingApproved> findBooking(long bookingId, long userId, long userId1);

    @Query("select new ru.practicum.shareit.booking.dto.BookingApproved(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking as b " +
            "where b.booker.id = ?1 and (b.status = ?2 or b.status = ?3)" +
            "order by b.start desc ")
    List<BookingApproved> findAllByStatus(long userId, Status status, Status status1);

    List<BookingApproved> findAllByBookerIdOrderByStartDesc(long userId);

    List<BookingApproved> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status state);

    @Query("select new ru.practicum.shareit.booking.dto.BookingApproved(b.id, b.start, b.finish, b.status, b.item, b.item.id, b.booker) " +
            "from Booking  as b " +
            "where b.item.owner.id = ?1 and (b.status = ?2 or b.status = ?3) " +
            "order by b.start desc ")
    List<BookingApproved> findAllByItemOwnerIdAndStatus(long userId, Status status, Status status1);

    List<BookingApproved> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status);

    List<BookingApproved> findAllByItemOwnerIdOrderByStartDesc(long userId);

    Optional<Booking> findByIdAndItemOwnerId(Long bookingId, Long userId);

    boolean existsByItemOwnerIdOrBookerId(Long userId, Long userId1);

    List<Booking> findAllByItemIdAndItemOwnerIdAndStatusNotOrderByStart(Long itemId, Long userId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStart(Long userId);

    Optional<Booking> findByItemIdAndBookerId(Long itemId, Long userId);

    List<Booking> findAllByItemIdAndItemOwnerIdAndStatusNot(Long itemId, Long userId, Status status);
}
