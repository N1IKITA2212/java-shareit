package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AlreadyExistsUserException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserPatchDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream().map(userMapper::toUserDto).toList();
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.toUserDto(userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException(
                "Пользователь с таким id не найден"
        )));
    }

    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.isUserWithEmailExist(userCreateDto.getEmail())) {
            throw new AlreadyExistsUserException("Пользователь с таким email уже существует");
        }
        return userMapper.toUserDto(userRepository.createUser(userMapper.fromCreateDto(userCreateDto)));
    }

    @Override
    public UserDto updateUser(Long userId, UserPatchDto userPatchDto) {
        if (!userRepository.isUserExists(userId)) {
            throw new NotFoundException("Пользователь с переданным id не существует");
        }
        if (userRepository.isUserWithEmailExist(userPatchDto.getEmail())) {
            throw new AlreadyExistsUserException("Пользователь с таким email уже существует");
        }
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        userMapper.applyPatch(userPatchDto, user);
        return userMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
