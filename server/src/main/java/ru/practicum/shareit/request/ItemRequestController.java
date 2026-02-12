package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestBody ItemRequestCreateDto itemRequestCreateDto,
                                        @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.createItemRequest(itemRequestCreateDto, requestorId);
    }

    @GetMapping
    public List<ItemRequestWithAnswersDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.getOwnRequests(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllOthersRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllOthersRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswersDto getRequestById(@PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestId);
    }
}
