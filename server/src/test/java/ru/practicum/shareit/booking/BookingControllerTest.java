package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;

    private BookingDto bookingDto;
    private BookingCreateDto bookingCreateDto;
    private LocalDateTime start = LocalDateTime.of(2026, 12, 12, 10, 10, 10);
    private LocalDateTime end = start.plusDays(2);
    private ItemDto itemDto;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("itemDesc");
        itemDto.setAvailable(true);
        itemDto.setId(1L);
        itemDto.setOwnerId(1L);

        userDto = new UserDto(1L, "user", "user@mail.com");

        bookingDto = new BookingDto();
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setId(1L);
        bookingDto.setItem(itemDto);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setBooker(userDto);

        bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(1L);
        bookingCreateDto.setStart(start);
        bookingCreateDto.setEnd(end);
    }

    @Test
    void createBookingTest() throws Exception {
        when(bookingService.createBooking(1L, bookingCreateDto))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value(start.toString()))
                .andExpect(jsonPath("$.end").value(end.toString()))
                .andExpect(jsonPath("$.item.id").value(1L))
                .andExpect(jsonPath("$.item.name").value("item"))
                .andExpect(jsonPath("$.booker.name").value("user"));

        verify(bookingService).createBooking(1L, bookingCreateDto);

    }

    @Test
    void changeBookingStatusTest() throws Exception {
        when(bookingService.changeBookingStatus(1L, "true", 1L))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value(start.toString()))
                .andExpect(jsonPath("$.end").value(end.toString()))
                .andExpect(jsonPath("$.item.id").value(1L))
                .andExpect(jsonPath("$.item.name").value("item"))
                .andExpect(jsonPath("$.booker.name").value("user"));

        verify(bookingService).changeBookingStatus(1L, "true", 1L);
    }

    @Test
    void changeBookingStatusWithoutHeader() throws Exception {
        when(bookingService.changeBookingStatus(1L, "abc", 1L))
                .thenThrow(new BadRequestException("bad request"));

        mvc.perform(patch("/bookings/1")
                        .param("approved", "abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingInfoTest() throws Exception {
        when(bookingService.getBookingInfo(1L, 1L))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").value(start.toString()))
                .andExpect(jsonPath("$.end").value(end.toString()))
                .andExpect(jsonPath("$.item.id").value(1L))
                .andExpect(jsonPath("$.item.name").value("item"))
                .andExpect(jsonPath("$.booker.name").value("user"));

        verify(bookingService).getBookingInfo(1L, 1L);
    }

    @Test
    void getAllUserBookingTest() throws Exception {
        when(bookingService.getAllUserBooking(State.ALL, 1L))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].start").value(start.toString()))
                .andExpect(jsonPath("$[0].end").value(end.toString()))
                .andExpect(jsonPath("$[0].item.id").value(1L))
                .andExpect(jsonPath("$[0].item.name").value("item"))
                .andExpect(jsonPath("$[0].booker.name").value("user"));
        verify(bookingService).getAllUserBooking(State.ALL, 1L);
    }

    @Test
    void getAllItemsBookingForUserTest() throws Exception {
        when(bookingService.getAllItemsBookingForUser(State.ALL, 1L))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].start").value(start.toString()))
                .andExpect(jsonPath("$[0].end").value(end.toString()))
                .andExpect(jsonPath("$[0].item.id").value(1L))
                .andExpect(jsonPath("$[0].item.name").value("item"))
                .andExpect(jsonPath("$[0].booker.name").value("user"));
        verify(bookingService).getAllItemsBookingForUser(State.ALL, 1L);
    }

}
