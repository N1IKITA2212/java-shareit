package ru.practicum.shareit.item.dto;

/**
 * TODO Sprint add-controllers.
 */

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private Long ownerId;
}
