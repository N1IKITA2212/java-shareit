package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status);

    List<Booking> findByItemOwnerId(Long userId);

    List<Booking> findByItemOwnerIdAndStartAfter(Long userId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndEndBefore(Long userId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(Long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findByItemOwnerIdAndStatus(Long userId, BookingStatus status);

    List<Booking> findByItemId(Long itemId);

    List<Booking> findByItemIdAndStatus(Long itemId, BookingStatus status);
}
