package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoJsonTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto booker = new UserDto(1L, "user", "user@mail.com");
        ItemDto itemDto = new ItemDto();
        itemDto.setOwnerId(2L);
        itemDto.setAvailable(true);
        itemDto.setId(3L);
        itemDto.setName("drill");
        itemDto.setDescription("power drill");

        BookingDto dto = new BookingDto();
        dto.setStatus(BookingStatus.APPROVED);
        dto.setEnd(LocalDateTime.of(2000, 1, 1, 1, 1));
        dto.setStart(LocalDateTime.of(1999, 12, 12, 12, 12));
        dto.setItem(itemDto);
        dto.setBooker(booker);
        dto.setId(4L);

        JsonContent<BookingDto> result = json.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(4);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("1999-12-12T12:12:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2000-01-01T01:01:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("user");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(3);
    }

    @Test
    void testDeserialize() throws Exception {

        BookingDto dto = json.readObject("booking.json");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2026, 2, 14, 10, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2026, 2, 14, 11, 0));
        assertThat(dto.getBooker().getId()).isEqualTo(2L);
        assertThat(dto.getBooker().getName()).isEqualTo("booker");
        assertThat(dto.getItem().getId()).isEqualTo(3L);
        assertThat(dto.getItem().getName()).isEqualTo("drill");
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}
