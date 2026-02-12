package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;

    private ItemDto itemDto;
    private ItemCreateDto itemCreateDto;
    private ItemPatchDto itemPatchDto;
    private ItemWithBookingDto itemWithBookingDto;
    private CommentCreateDto commentCreateDto;
    private CommentDto commentDto;
    private LocalDateTime time = LocalDateTime.now();

    @BeforeEach
    void beforeEach() {
        itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("itemDesc");
        itemDto.setAvailable(true);
        itemDto.setId(1L);
        itemDto.setOwnerId(1L);

        itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("itemCreate");
        itemCreateDto.setDescription("itemCreateDesc");
        itemCreateDto.setIsAvailable(true);

        itemPatchDto = new ItemPatchDto();
        itemPatchDto.setName("itemPatch");
        itemPatchDto.setDescription("itemPatchDesc");
        itemPatchDto.setIsAvailable(true);

        itemWithBookingDto = new ItemWithBookingDto();
        itemWithBookingDto.setId(1L);
        itemWithBookingDto.setOwnerId(1L);
        itemWithBookingDto.setAvailable(true);
        itemWithBookingDto.setName("itemWithBooking");
        itemWithBookingDto.setDescription("itemWithBookingDesc");

        commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("text");

        commentDto = new CommentDto();
        commentDto.setAuthorName("author");
        commentDto.setId(1L);
        commentDto.setCreated(time);
        commentDto.setText("text");
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(1L, 1L))
                .thenReturn(itemWithBookingDto);

        mvc.perform(get("/items/1")
                        .header("X-sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(itemService).getItemById(1L, 1L);
    }

    @Test
    void getItemByIdWithoutHeaderTest() throws Exception {
        mvc.perform(get("/items/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserItemsTest() throws Exception {
        when(itemService.getUserItems(1L)).thenReturn(List.of(itemWithBookingDto));

        mvc.perform(get("/items")
                        .header("X-sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("itemWithBooking"));

        verify(itemService).getUserItems(1L);
    }

    @Test
    void createItemTest() throws Exception {
        when(itemService.createItem(1L, itemCreateDto))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("item"))
                .andExpect(jsonPath("$.description").value("itemDesc"))
                .andExpect(jsonPath("$.ownerId").value(1L));

        verify(itemService).createItem(1L, itemCreateDto);
    }

    @Test
    void createItemWrongUserId() throws Exception {
        when(itemService.createItem(anyLong(), any()))
                .thenThrow(new NotFoundException("User not found"));

        mvc.perform(post("/items")
                        .header("X-sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(1L, itemPatchDto, 1L))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .header("X-sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemPatchDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("item"))
                .andExpect(jsonPath("$.description").value("itemDesc"))
                .andExpect(jsonPath("$.ownerId").value(1L));

        verify(itemService).updateItem(1L, itemPatchDto, 1L);
    }

    @Test
    void searchItemTest() throws Exception {
        when(itemService.searchItem("item"))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                .param("text", "item")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("item"))
                .andExpect(jsonPath("$[0].description").value("itemDesc"));

        verify(itemService).searchItem("item");
    }

    @Test
    void createCommentTest() throws Exception {
        when(itemService.createComment(commentCreateDto, 1L, 1L))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                .header("X-sharer-User-Id", 1L)
                .content(mapper.writeValueAsString(commentCreateDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.authorName").value("author"));

        verify(itemService).createComment(commentCreateDto, 1L, 1L);
    }
}
