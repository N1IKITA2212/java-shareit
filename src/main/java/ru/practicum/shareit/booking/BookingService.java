package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(Long bookerId, BookingCreateDto bookingCreateDto);

    BookingDto changeBookingStatus(Long bookingId, BookingStatus bookingStatus, Long userId);

    BookingDto getBookingInfo(Long bookingId, Long userId);

    List<BookingDto> getAllUserBooking(State state, Long userId);

    List<BookingDto> getAllItemsBookingForUser(State state, Long userId);
}
