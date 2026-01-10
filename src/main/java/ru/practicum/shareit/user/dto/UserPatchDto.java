package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserPatchDto {
    @Email(message = "Не соответствует email")
    private String email;
    private String name;
}
