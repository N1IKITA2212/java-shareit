package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(ItemRequestCreateDto itemRequestCreateDto, Long requestorId);

    List<ItemRequestWithAnswersDto> getOwnRequests(Long requestorId);

    List<ItemRequestDto> getAllOthersRequests(Long userId);

    ItemRequestWithAnswersDto getRequestById(Long requestId);
}
