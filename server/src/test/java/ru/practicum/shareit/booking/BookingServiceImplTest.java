package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookingServiceImplTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void getAllItemsBookingForUserTest() {
        User owner = new User();
        owner.setName("owner");
        owner.setEmail("owner@email.com");

        owner = userRepository.save(owner);

        User booker = new User();
        booker.setName("booker");
        booker.setEmail("booker@email.com");

        booker = userRepository.save(booker);

        Item item = new Item();
        item.setItemRequest(null);
        item.setAvailable(true);
        item.setName("Drill");
        item.setDescription("Power Drill");
        item.setOwner(owner);

        item = itemRepository.save(item);

        LocalDateTime now = LocalDateTime.now();

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(now.plusDays(1));
        booking.setEnd(now.plusDays(3));
        booking.setStatus(BookingStatus.APPROVED);

        booking = bookingRepository.save(booking);

        List<BookingDto> result = bookingService.getAllItemsBookingForUser(State.ALL, owner.getId());

        assertThat(1, equalTo(result.size()));

        BookingDto dto = result.getFirst();

        assertThat(booking.getId(), equalTo(dto.getId()));
        assertThat(booking.getBooker().getId(), equalTo(dto.getBooker().getId()));
        assertThat(booking.getItem().getId(), equalTo(dto.getItem().getId()));
        assertThat(booking.getStart(), equalTo(dto.getStart()));
        assertThat(booking.getEnd(), equalTo(dto.getEnd()));
    }
}
