package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(Long itemId);

    List<ItemDto> getUserItems(Long ownerId);

    ItemDto createItem(Long ownerId, ItemCreateDto itemCreateDto);

    ItemDto updateItem(Long ownerId, ItemPatchDto itemPatchDto, Long itemId);

    List<ItemDto> searchItem(String text);
}
