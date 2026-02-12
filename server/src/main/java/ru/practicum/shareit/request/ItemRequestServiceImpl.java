package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(ItemRequestCreateDto itemRequestCreateDto, Long requestorId) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + requestorId + " не найден"));
        ItemRequest itemRequest = itemRequestMapper.fromItemRequestCreateDto(itemRequestCreateDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(requestor);

        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestWithAnswersDto> getOwnRequests(Long requestorId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreatedAsc(requestorId);
        List<Long> requestIds = itemRequests.stream().map(ItemRequest::getId).toList();

        Map<Long, List<ItemAnswerDto>> answerByRequestId = itemRepository.findByItemRequestIdIn(requestIds)
                .stream().map(itemMapper::toItemAnswerDto)
                .collect(Collectors.groupingBy(ItemAnswerDto::getRequestId));

        return itemRequests.stream()
                .map(itemRequest -> itemRequestMapper.toItemRequestWithAnswersDto(
                        itemRequest, answerByRequestId.getOrDefault(itemRequest.getId(), List.of())
                )).toList();
    }

    @Override
    public List<ItemRequestDto> getAllOthersRequests(Long userId) {
        return itemRequestRepository.findAllOthers(userId).stream()
                .map(itemRequestMapper::toItemRequestDto).toList();
    }

    @Override
    public ItemRequestWithAnswersDto getRequestById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + requestId + " не найден"));
        List<ItemAnswerDto> itemsByRequest = itemRepository.findByItemRequestId(requestId)
                .stream().map(itemMapper::toItemAnswerDto).toList();
        return itemRequestMapper.toItemRequestWithAnswersDto(itemRequest, itemsByRequest);


    }
}
