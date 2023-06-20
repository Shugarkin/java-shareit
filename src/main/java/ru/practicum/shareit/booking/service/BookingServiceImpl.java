package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSearch;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.TimeErrorException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        return bookingRepository.save(booking);
    }

    @Override
    public BookingSearch findBooking(long userId, long bookingId) {
        return bookingRepository.findBooking(bookingId, userId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));
    }

    @Override
    public List<BookingSearch> findListBooking(long userId, Status state) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден"));
        if (state == null) {
            state = Status.ALL;
        }
        if (state.equals(Status.CURRENT)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                    .filter(a -> a.getFinish().isAfter(LocalDateTime.now()))
                    .filter(a -> a.getStart().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
            return list;
        } else if (state.equals(Status.PAST)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                    .filter(a -> a.getStatus().equals(Status.APPROVED))
                    .filter(a -> a.getFinish().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
            return list;
        } else if (state.equals(Status.FUTURE)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                    .filter(a -> a.getStart().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());

            return list;
        } else if (state.equals(Status.WAITING)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            return list;
        } else if (state.equals(Status.REJECTED)) {
            List<BookingSearch> list = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            return list;
        } else if (state.equals(Status.ALL)) {
            return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        }
        throw new EntityNotFoundException("Неверный запрос");

    }

    @Override
    public List<BookingSearch> findListOwnerBooking(long userId, Status state) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Юзер не найден"));
        if (state == null) {
            state = Status.ALL;
        }
        if (state.equals(Status.CURRENT)) {
            List<BookingSearch> list = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId).stream()
                    .filter(a -> a.getFinish().isAfter(LocalDateTime.now()))
                    .filter(a -> a.getStart().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
            return list;
        } else if (state.equals(Status.PAST)) {
            List<BookingSearch> list =  bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.APPROVED).stream()
                    .filter(a -> a.getStatus().equals(Status.APPROVED))
                    .filter(a -> a.getFinish().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
            return list;
        } else if (state.equals(Status.FUTURE)) {
            List<BookingSearch> list =  bookingRepository.findAllByItemOwnerIdAndStatus(userId, Status.WAITING, Status.APPROVED).stream()
                    .filter(a -> a.getStart().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
            return list;
        } else if (state.equals(Status.WAITING)) {
            return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
        } else if (state.equals(Status.REJECTED)) {
            return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
        } else if (state.equals(Status.ALL)) {
            return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
        }
        throw new EntityNotFoundException("Неверный запрос");
    }

}
