package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getUsersItem(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getUserItems(ownerId).stream().map(itemMapper::toItemDto).toList();
    }

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemCreateDto itemCreateDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemMapper.toItemDto(itemService.createItem(ownerId, itemCreateDto));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text).stream().map(itemMapper::toItemDto).toList();
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemPatchDto itemPatchDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @PathVariable Long itemId) {
        return itemMapper.toItemDto(itemService.updateItem(ownerId, itemPatchDto, itemId));
    }

}
