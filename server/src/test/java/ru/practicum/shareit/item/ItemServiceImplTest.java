package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ItemServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemService itemService;

    @Test
    void getUserItemsShouldReturnItemWithLastAndNextBooking() {
        User owner = new User();
        owner.setName("owner");
        owner.setEmail("owner@mail.com");
        owner = userRepository.save(owner);

        User booker = new User();
        booker.setName("booker");
        booker.setEmail("booker@mail.com");
        booker = userRepository.save(booker);

        Item item = new Item();
        item.setName("Drill");
        item.setDescription("Power drill");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setItemRequest(null);

        item = itemRepository.save(item);

        LocalDateTime now = LocalDateTime.now();

        Booking pastBooking = new Booking();
        pastBooking.setStart(now.minusDays(5));
        pastBooking.setEnd(now.minusDays(3));
        pastBooking.setItem(item);
        pastBooking.setBooker(booker);
        pastBooking.setStatus(BookingStatus.APPROVED);
        pastBooking = bookingRepository.save(pastBooking);

        Booking nextBooking = new Booking();
        nextBooking.setStart(now.plusDays(1));
        nextBooking.setEnd(now.plusDays(3));
        nextBooking.setItem(item);
        nextBooking.setBooker(booker);
        nextBooking.setStatus(BookingStatus.APPROVED);
        nextBooking = bookingRepository.save(nextBooking);

        Comment comment = new Comment();
        comment.setText("good item");
        comment.setAuthor(booker);
        comment.setItem(item);
        comment.setCreated(now.minusDays(1));
        comment = commentRepository.save(comment);

        List<ItemWithBookingDto> result = itemService.getUserItems(owner.getId());

        assertThat(1, equalTo(result.size()));

        ItemWithBookingDto dto = result.getFirst();

        assertThat(item.getId(), equalTo(dto.getId()));

        assertThat(dto.getLastBooking(), notNullValue());
        assertThat(dto.getNextBooking(), notNullValue());

        assertThat(pastBooking.getId(), equalTo(dto.getLastBooking().getId()));
        assertThat(nextBooking.getId(), equalTo(dto.getNextBooking().getId()));

        assertThat(1, equalTo(dto.getComments().size()));

    }
}
