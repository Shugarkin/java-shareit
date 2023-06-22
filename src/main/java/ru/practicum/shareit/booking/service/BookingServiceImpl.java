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
            throw new AvailableException("Предмет не доступен для брони"); //не понятно почему в тестах есть разделение на эти две ошибки
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
        if (state.equals(State.CURRENT)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdAndStateCurrent(userId);
            return list;
        } else if (state.equals(State.PAST)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdAndStatePast(userId, Status.APPROVED);
            return list;
        } else if (state.equals(State.FUTURE)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdAndStateFuture(userId);
            return list;
        } else if (state.equals(State.WAITING)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            return list;
        } else if (state.equals(State.REJECTED)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            return list;
        } else if (state.equals(State.ALL)) {
            return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        }
        throw new EntityNotFoundException("Неверный запрос");

    }

    @Override
    public List<BookingSearch> findListOwnerBooking(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден"));
        if (state.equals(State.CURRENT)) {
            List<BookingSearch> list = bookingRepository.findAllByItemOwnerAndStateCurrent(userId);
            return list;
        } else if (state.equals(State.PAST)) {
            List<BookingSearch> list =  bookingRepository.findAllByItemOwnerIdAndStatePast(userId, Status.APPROVED);
            return list;
        } else if (state.equals(State.FUTURE)) {
            List<BookingSearch> list =  bookingRepository.findAllByItemOwnerIdAndStateFuture(userId, Status.REJECTED);
            return list;
        } else if (state.equals(State.WAITING)) {
            return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
        } else if (state.equals(State.REJECTED)) {
            return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        } else if (state.equals(State.ALL)) {
            return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
        }
        throw new EntityNotFoundException("Неверный запрос");
    }

}
