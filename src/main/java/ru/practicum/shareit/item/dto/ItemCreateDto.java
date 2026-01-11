package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemCreateDto {
    @NotBlank(message = "Имя предмета должно быть передано")
    private String name;
    @NotBlank(message = "Описание предмета должно быть передано")
    private String description;
    @JsonProperty("available")
    @NotNull(message = "Статус предмета должен быть передан")
    private Boolean isAvailable;
}
