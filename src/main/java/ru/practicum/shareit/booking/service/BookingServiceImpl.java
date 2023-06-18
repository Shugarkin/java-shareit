package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingApproved;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.TimeErrorException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    public Booking postBooking(Long userId, Booking booking) {
        if (booking.getStart().isAfter(booking.getFinish()) || booking.getStart().equals(booking.getFinish())) {
            throw new TimeErrorException("Патруль времени выехал по вашему адресу");
        }
        Item item = itemRepository.findById(booking.getItemId())
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

    @Override
    public Booking approvedBooking(Long userId, Long bookingId, Boolean answer) {
        boolean answer1 = bookingRepository.existsByItemOwnerIdOrBookerId(userId, userId);
        if (answer1 == false) {
            throw new AvailableException("ты не имеешь права, о ты не имеешь права");
        }
        Booking b = bookingRepository.findByIdAndItemOwnerId(bookingId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Бронь не найдена"));
        if (!b.getStatus().equals(Status.WAITING)) {
            throw new AvailableException("Предмет не может быть одобрен");
        }
        if (answer == true) {
            b.setStatus(Status.APPROVED);
        } else {
            b.setStatus(Status.REJECTED);
        }

        return bookingRepository.save(b);
    }

    @Override
    public BookingApproved findBooking(long userId, long bookingId) {
        return bookingRepository.findBooking(bookingId, userId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
    }

    @Override
    public List<BookingApproved> findListBooking(long userId, Status state) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден"));
        if (state == null) {
            state = Status.ALL;
        }
        if (state.equals(Status.CURRENT)) {
            return bookingRepository.findAllByStatus(userId, Status.REJECTED, Status.APPROVED);
        } else if (state.equals(Status.PAST)) {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.APPROVED);
        } else if (state.equals(Status.FUTURE)) {
            return bookingRepository.findAllByStatus(userId, Status.WAITING, Status.APPROVED);
        } else if (state.equals(Status.WAITING)) {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
        } else if (state.equals(Status.REJECTED)) {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        } else if (state.equals(Status.ALL)){
            return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        }
        throw new EntityNotFoundException("Неверный запрос");

    }

    @Override
    public List<BookingApproved> findListOwnerBooking(long userId, Status state) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден"));
        if (state == null) {
            state = Status.ALL;
        }
        if (state.equals(Status.CURRENT)) {
            return bookingRepository.findAllByItemOwnerIdAndStatus(userId, Status.REJECTED, Status.APPROVED);
        } else if (state.equals(Status.PAST)) {
            return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.APPROVED);
        } else if (state.equals(Status.FUTURE)) {
            return bookingRepository.findAllByItemOwnerIdAndStatus(userId, Status.WAITING, Status.APPROVED);
        } else if (state.equals(Status.WAITING)) {
            return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
        } else if (state.equals(Status.REJECTED)) {
            return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        } else if (state.equals(Status.ALL)){
            return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
        }
        throw new EntityNotFoundException("Неверный запрос");
    }

}
