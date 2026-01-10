package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(of = {"id", "email"})
public class User {
    private Long id;
    private String name;
    private String email;
}
