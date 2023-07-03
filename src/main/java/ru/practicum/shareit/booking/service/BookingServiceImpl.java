package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public Booking postBooking(Long userId, Booking booking) {
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));
        if (item.getAvailable().equals(false)) {
            throw new AvailableException("Предмет не доступен для брони");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new EntityNotFoundException("Раздюпать вещь не получится");
        }
        booking.setItem(item);
        booking.setBooker(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking approvedBooking(Long userId, Long bookingId, Boolean answer) {
        boolean isBookingExist = bookingRepository.existsByItemOwnerIdOrBookerId(userId, userId);
        if (isBookingExist == false) {
            throw new AvailableException("ты не имеешь права, о ты не имеешь права");
        }
        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Бронь не найдена"));
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new AvailableException("Предмет не может быть одобрен");
        }
        if (answer == true) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return booking;
    }

    @Override
    public BookingSearch findBooking(long userId, long bookingId) {
        return bookingRepository.findBooking(bookingId, userId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
    }

    @Override
    public List<BookingSearch> findListBooking(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден"));
        switch (state) {
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStateCurrent(userId);
            case PAST:
                return bookingRepository.findAllByBookerIdAndStatePast(userId, Status.APPROVED);
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStateFuture(userId);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case ALL:
                List<BookingSearch> allByBookerIdOrderByStartDesc = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            default:
                throw new EntityNotFoundException("Неверный запрос");
        }
    }

    @Override
    public List<BookingSearch> findListOwnerBooking(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден"));
        switch (state) {
            case CURRENT:
                return bookingRepository.findAllByItemOwnerAndStateCurrent(userId);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndStatePast(userId, Status.APPROVED);
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStateFuture(userId, Status.REJECTED);
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            default:
                throw new EntityNotFoundException("Неверный запрос");
        }
    }

}
