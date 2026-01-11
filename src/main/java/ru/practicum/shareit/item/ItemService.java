package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item getItemById(Long itemId);

    List<Item> getUserItems(Long ownerId);

    Item createItem(Long ownerId, ItemCreateDto itemCreateDto);

    Item updateItem(Long ownerId, ItemPatchDto itemPatchDto, Long itemId);

    List<Item> searchItem(String text);
}
