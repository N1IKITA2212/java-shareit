package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody BookingCreateDto bookingCreateDto) {
        return bookingClient.createBooking(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable Long bookingId,
                                                      @RequestParam String approved) {
        return bookingClient.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingInfo(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId) {
        return bookingClient.getBookingInfo(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getAllUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllItemsBookingForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getAllItemsBookingForUser(userId, state);
    }
}
