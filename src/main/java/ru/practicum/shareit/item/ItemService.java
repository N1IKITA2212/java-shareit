package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemWithBookingDto getItemById(Long itemId, Long ownerId);

    List<ItemWithBookingDto> getUserItems(Long ownerId);

    ItemDto createItem(Long ownerId, ItemCreateDto itemCreateDto);

    ItemDto updateItem(Long ownerId, ItemPatchDto itemPatchDto, Long itemId);

    List<ItemDto> searchItem(String text);

    CommentDto createComment(CommentCreateDto commentCreateDto, Long authorId, Long itemId);
}
