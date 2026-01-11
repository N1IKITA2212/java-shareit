package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwnerId()
        );
    }

    public Item fromCreateDto(ItemCreateDto itemCreateDto) {
        Item item = new Item();
        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setAvailable(itemCreateDto.getIsAvailable());
        return item;
    }

    public void applyPatch(ItemPatchDto itemPatchDto, Item item) {
        if (itemPatchDto.getName() != null) {
            item.setName(itemPatchDto.getName());
        }
        if (itemPatchDto.getDescription() != null) {
            item.setDescription(itemPatchDto.getDescription());
        }
        if (itemPatchDto.getIsAvailable() != null) {
            item.setAvailable(itemPatchDto.getIsAvailable());
        }
    }
}
