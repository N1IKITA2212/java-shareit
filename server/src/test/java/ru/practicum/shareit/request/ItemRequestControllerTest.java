package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private ObjectMapper mapper;

    private ItemRequestDto itemRequestDto;
    private ItemRequestCreateDto itemRequestCreateDto;
    private ItemRequestWithAnswersDto itemRequestWithAnswersDto;
    private ItemAnswerDto itemAnswerDto;

    private LocalDateTime created = LocalDateTime.of(2026, 12, 12, 10, 10, 10);

    @BeforeEach
    void beforeEach() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setCreated(created);
        itemRequestDto.setDescription("desc");

        itemRequestCreateDto = new ItemRequestCreateDto();
        itemRequestCreateDto.setDescription("createDesc");

        itemAnswerDto = new ItemAnswerDto();
        itemAnswerDto.setOwnerId(1L);
        itemAnswerDto.setId(1L);
        itemAnswerDto.setRequestId(1L);
        itemAnswerDto.setName("item");

        itemRequestWithAnswersDto = new ItemRequestWithAnswersDto();
        itemRequestWithAnswersDto.setItems(List.of(itemAnswerDto));
        itemRequestWithAnswersDto.setId(1L);
        itemRequestWithAnswersDto.setCreated(created);
        itemRequestWithAnswersDto.setDescription("desc");

    }

    @Test
    void createRequestTest() throws Exception {
        when(itemRequestService.createItemRequest(itemRequestCreateDto, 1L))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.created").value(created.toString()));

        verify(itemRequestService).createItemRequest(itemRequestCreateDto, 1L);
    }

    @Test
    void createRequestWithoutHeaderTest() throws Exception {
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnRequestsTest() throws Exception {
        when(itemRequestService.getOwnRequests(1L))
                .thenReturn(List.of(itemRequestWithAnswersDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].created").value(created.toString()))
                .andExpect(jsonPath("$[0].items.length()").value(1))
                .andExpect(jsonPath("$[0].items[0].name").value("item"));

        verify(itemRequestService).getOwnRequests(1L);

    }

    @Test
    void getAllOtherRequests() throws Exception {
        when(itemRequestService.getAllOthersRequests(2L))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].created").value(created.toString()));

        verify(itemRequestService).getAllOthersRequests(2L);

    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(1L))
                .thenReturn(itemRequestWithAnswersDto);

        mvc.perform(get("/requests/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.created").value(created.toString()))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].name").value("item"));

        verify(itemRequestService).getRequestById(1L);
    }

    @Test
    void getRequestByNotFoundIdTest() throws Exception {
        when(itemRequestService.getRequestById(anyLong()))
                .thenThrow(new NotFoundException("request not found"));

        mvc.perform(get("/requests/1"))
                .andExpect(status().isNotFound());
    }
}
