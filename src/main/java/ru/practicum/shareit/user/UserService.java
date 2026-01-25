package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(UserCreateDto userCreateDto);

    UserDto updateUser(Long userId, UserPatchDto userPatchDto);

    void deleteUser(Long userId);
}
