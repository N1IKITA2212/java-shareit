package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setOwnerId(item.getOwner().getId());

        if (item.getItemRequest() != null) {
            itemDto.setRequestId(item.getItemRequest().getId());
        }
        return itemDto;
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

    public ItemWithBookingDto toItemWithBookingDto(Item item, List<CommentDto> comments, BookingDto lastBooking,
                                                   BookingDto nextBooking) {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto();
        itemWithBookingDto.setId(item.getId());
        itemWithBookingDto.setName(item.getName());
        itemWithBookingDto.setDescription(item.getDescription());
        itemWithBookingDto.setOwnerId(item.getOwner().getId());
        itemWithBookingDto.setAvailable(item.isAvailable());
        itemWithBookingDto.setComments(comments);
        itemWithBookingDto.setLastBooking(lastBooking);
        itemWithBookingDto.setNextBooking(nextBooking);
        return itemWithBookingDto;
    }

    public ItemAnswerDto toItemAnswerDto(Item item) {
        ItemAnswerDto itemAnswerDto = new ItemAnswerDto();
        itemAnswerDto.setId(item.getId());
        itemAnswerDto.setName(item.getName());
        itemAnswerDto.setOwnerId(item.getOwner().getId());
        return itemAnswerDto;
    }
}
