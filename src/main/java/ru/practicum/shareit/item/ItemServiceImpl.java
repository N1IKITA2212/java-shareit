package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessViolationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден")));
    }

    @Override
    public List<ItemDto> getUserItems(Long ownerId) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        return itemRepository.findByOwnerId(ownerId).stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto createItem(Long ownerId, ItemCreateDto itemCreateDto) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        Item item = itemMapper.fromCreateDto(itemCreateDto);
        item.setOwnerId(ownerId);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long ownerId, ItemPatchDto itemPatchDto, Long itemId) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));
        if (!Objects.equals(item.getOwnerId(), ownerId)) {
            throw new AccessViolationException("Редактирование данного предмета недоступно");
        }
        itemMapper.applyPatch(itemPatchDto, item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByIsAvailableTrueAndNameContainingIgnoreCaseOrIsAvailableTrueAndDescriptionContainingIgnoreCase(text, text)
                .stream().map(itemMapper::toItemDto).toList();
    }
}
