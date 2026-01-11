package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class Item {
    private Long id;
    private String name;
    private String description;
    private boolean isAvailable;
    private Long ownerId;
}
