package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long userId);

    User createUser(UserCreateDto userCreateDto);

    User updateUser(Long userId, UserPatchDto userPatchDto);

    void deleteUser(Long userId);
}
