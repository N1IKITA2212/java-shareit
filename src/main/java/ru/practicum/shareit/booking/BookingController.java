package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.BadRequestException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody @Valid BookingCreateDto bookingCreateDto,
                                    @RequestHeader(name = "X-Sharer-User-Id") Long bookerId) {
        return bookingService.createBooking(bookerId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeBookingStatus(@PathVariable Long bookingId,
                                          @RequestParam String approved,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        BookingStatus bookingStatus;
        if (approved.equalsIgnoreCase("true")) {
            bookingStatus = BookingStatus.APPROVED;
        } else if (approved.equalsIgnoreCase("false")) {
            bookingStatus = BookingStatus.REJECTED;
        } else {
            throw new BadRequestException("Неверное значение параметра 'approved': " + approved
                    + ". Должно быть true или false");
        }
        return bookingService.changeBookingStatus(bookingId, bookingStatus, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInfo(@PathVariable Long bookingId,
                                     @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingInfo(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllUserBooking(@RequestParam(defaultValue = "ALL") State state,
                                              @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.getAllUserBooking(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookingForUser(@RequestParam(defaultValue = "ALL") State state,
                                                @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.getAllItemsBookingForUser(state, userId);
    }
}
