package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestSearch;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private long userId = 1L;

    private long itemRequestId = 1L;

    private User user = User.builder().id(userId).name("dgs").email("ff@mail.com").build();

    private ItemRequest itemRequest = ItemRequest.builder()
            .created(LocalDateTime.now())
            .userId(userId)
            .id(itemRequestId)
            .description("asd")
            .build();

    @BeforeEach
    void before() {
        user = userRepository.save(user);
        userId = user.getId();

        itemRequest.setUserId(userId);
        itemRequest = itemRequestRepository.save(itemRequest);
        itemRequestId = itemRequest.getId();
    }

    @AfterEach
    void after() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void findAllByUserIdTest() {
        List<ItemRequestSearch> allByUserId = itemRequestRepository.findAllByUserId(userId);

        assertEquals(1, allByUserId.size());
    }

    @Test
    void findAllByUserIdNotOrderByCreatedTest() {
        long otherUser = userId + 1;
        Pageable pageable = PageRequest.of(0, 1);
        List<ItemRequestSearch> allByUserIdNotOrderByCreated = itemRequestRepository.findAllByUserIdNotOrderByCreated(otherUser, pageable);

        assertEquals(1, allByUserIdNotOrderByCreated.size());
    }

    @Test
    void findByIdTest() {
        Optional<ItemRequestSearch> byId = itemRequestRepository.findById(itemRequestId);

        Assertions.assertNotNull(byId);
    }
}
