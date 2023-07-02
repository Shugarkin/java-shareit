package ru.practicum.shareit.Item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    private void addItem() {
        long itemId = 1L;
        long userId = 1L;
        long itemRequestId = 1L;
        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        userRepository.save(user);

        ItemRequest itemRequest = ItemRequest.builder()
                .description("qwe").id(itemRequestId).userId(userId).created(LocalDateTime.now()).build();
        itemRequestRepository.save(itemRequest);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").requestId(1L).owner(user).build();
        itemRepository.save(item);

    }

    @AfterEach
    private void delete() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findAllByOwnerIdTest() {
        long userId = 1L;

        List<Item> item = itemRepository.findAllByOwnerId(userId);

        assertNotNull(item);
    }

    @Test
    void findItemSearchTest() {
        String text = "as";

        List<ItemSearch> itemSearch = itemRepository.findItemSearch(text, text);

        assertEquals(1, itemSearch.size());
    }

    @Test
    void findByIdAndOwnerIdTest() {
        long itemId = 1L;
        long userId = 1L;
        Optional<Item> byIdAndOwnerId = itemRepository.findByIdAndOwnerId(itemId, userId);

        assertTrue(byIdAndOwnerId.isPresent());
    }

    @Test
    void findAllByRequestIdTest() {
        List<Long> list = List.of(1L);
        List<Item> allByRequestId = itemRepository.findAllByRequestId(list);

        assertEquals(1, allByRequestId.size());
    }

}
