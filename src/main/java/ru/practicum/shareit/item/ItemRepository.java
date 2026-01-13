package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long itemId = 1L;

    public List<Item> getUserItems(Long ownerId) {
        return items.values().stream().filter(item -> item.getOwnerId().equals(ownerId)).toList();
    }

    public Optional<Item> getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    public Item createItem(Item item) {
        item.setId(itemId++);
        items.put(item.getId(), item);
        return item;
    }

    public List<Item> searchItem(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::isAvailable).toList();
    }
}
