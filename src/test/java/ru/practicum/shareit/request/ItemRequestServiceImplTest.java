package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void getOwnRequestsTest() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@email.com");

        user = userRepository.save(user);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user);
        itemRequest.setDescription("desc");
        itemRequest.setCreated(LocalDateTime.now());

        itemRequest = itemRequestRepository.save(itemRequest);

        List<ItemRequestWithAnswersDto> result = itemRequestService.getOwnRequests(user.getId());

        assertThat(1, equalTo(result.size()));

        ItemRequestWithAnswersDto dto = result.getFirst();

        assertThat(dto.getId(), equalTo(itemRequest.getId()));
        assertThat(dto.getCreated(), equalTo(itemRequest.getCreated()));
        assertThat(dto.getDescription(), equalTo(itemRequest.getDescription()));

    }
}
