package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessViolationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
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
    public Item getItemById(Long itemId) {
        return itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));
    }

    @Override
    public List<Item> getUserItems(Long ownerId) {
       if (!userRepository.isUserExists(ownerId)) {
           throw new NotFoundException("Пользователь с таким id не найден");
       }
       return itemRepository.getUserItems(ownerId);
    }

    @Override
    public Item createItem(Long ownerId, ItemCreateDto itemCreateDto) {
        if (!userRepository.isUserExists(ownerId)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        Item item = itemMapper.fromCreateDto(itemCreateDto);
        item.setOwnerId(ownerId);
        return itemRepository.createItem(item);
    }

    @Override
    public Item updateItem(Long ownerId, ItemPatchDto itemPatchDto, Long itemId) {
        if (!userRepository.isUserExists(ownerId)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));
        if (!Objects.equals(item.getOwnerId(), ownerId)) {
            throw new AccessViolationException("Редактирование данного предмета недоступно");
        }
        itemMapper.applyPatch(itemPatchDto, item);
        return item;
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text);
    }
}
