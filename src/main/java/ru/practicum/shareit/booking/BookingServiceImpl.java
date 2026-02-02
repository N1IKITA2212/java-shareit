package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.AccessViolationException;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto createBooking(Long bookerId, BookingCreateDto bookingCreateDto) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
        if (Objects.equals(booker.getId(), item.getOwner().getId())) {
            throw new BadRequestException("Владелец вещи не может создать запрос на бронирование собственной вещи");
        }
        if (!item.isAvailable()) {
            throw new BadRequestException("Данная вещь сейчас недоступна");
        }
        Booking booking = bookingMapper.fromBookingCreateDto(bookingCreateDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto changeBookingStatus(Long bookingId, BookingStatus bookingStatus, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос на бронирование не найден"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new AccessViolationException("Изменить статус бронирования может только владелец вещи");
        }
        booking.setStatus(bookingStatus);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingInfo(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос на бронирование не найден"));
        if (Objects.equals(booking.getBooker().getId(), userId) ||
                Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            return bookingMapper.toBookingDto(booking);
        }
        throw new AccessViolationException("Действие доступно только для владельца вещи или автора бронирования");
    }

    @Override
    public List<BookingDto> getAllUserBooking(State state, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBookerId(userId);
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfter(userId, now);
            case PAST -> bookingRepository.findByBookerIdAndEndBefore(userId, now);
            case CURRENT -> bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId, now, now);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED);
        };
        return bookings.stream().map(bookingMapper::toBookingDto).toList();
    }

    @Override
    public List<BookingDto> getAllItemsBookingForUser(State state, Long userId) {
        if (itemRepository.findByOwnerId(userId).isEmpty()) {
            throw new AccessViolationException("У этого пользователя отсутствуют вещи");
        }
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByItemOwnerId(userId);
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartAfter(userId, now);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBefore(userId, now);
            case CURRENT -> bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(userId, now, now);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED);
        };
        return bookings.stream().map(bookingMapper::toBookingDto).toList();
    }
}
