package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemAnswerDto {
    private Long id;
    private String name;
    private Long ownerId;
    private Long requestId;
}
