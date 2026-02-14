package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    void updateUserTest() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@mail.com");
        user = userRepository.save(user);

        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setName("newUser");
        userPatchDto.setEmail("newEmail@mail.com");

        UserDto userDto = userService.updateUser(user.getId(), userPatchDto);

        assertThat(user.getName(), equalTo(userPatchDto.getName()));
        assertThat(user.getEmail(), equalTo(userPatchDto.getEmail()));

        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }
}
