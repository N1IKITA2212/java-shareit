package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@Component
public class ItemRequestMapper {

    public ItemRequest fromItemRequestCreateDto(ItemRequestCreateDto itemRequestCreateDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestCreateDto.getDescription());
        return itemRequest;
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public ItemRequestWithAnswersDto toItemRequestWithAnswersDto(ItemRequest itemRequest, List<ItemAnswerDto> itemAnswerDtoList) {
        ItemRequestWithAnswersDto itemRequestWithAnswersDto = new ItemRequestWithAnswersDto();
        itemRequestWithAnswersDto.setId(itemRequest.getId());
        itemRequestWithAnswersDto.setDescription(itemRequest.getDescription());
        itemRequestWithAnswersDto.setCreated(itemRequest.getCreated());
        itemRequestWithAnswersDto.setItems(itemAnswerDtoList);
        return itemRequestWithAnswersDto;
    }
}
