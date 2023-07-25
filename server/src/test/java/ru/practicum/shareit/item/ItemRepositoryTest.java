package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import org.springframework.data.domain.Pageable;
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

    //изначально хотел воспользоваться методами beforeEach, afterEach, но из-за присвоения бд id
    // не всегда можно отловить в каком методе какой id будет у сущности


    private long userId = 1L;
    private long itemId = 1L;

    private long itemRequestId = 1L;
    private Pageable pageable = PageRequest.of(0, 10);

    private User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();

    private Item item = Item.builder()
            .id(itemId).name("asd").available(true).description("asdf").requestId(itemRequestId).owner(user).build();

    private ItemRequest itemRequest = ItemRequest.builder()
            .description("qwe").id(itemRequestId).userId(userId).created(LocalDateTime.now().withNano(0)).build();

    @BeforeEach
    void before() {
        user = userRepository.save(user);
        userId = user.getId();

        itemRequest.setUserId(userId);
        itemRequest = itemRequestRepository.save(itemRequest);
        itemRequestId = itemRequest.getId();

        item.setOwner(user);
        item.setRequestId(itemRequestId);
        item = itemRepository.save(item);
        itemId = item.getId();
    }

    @AfterEach
    void after() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findAllByOwnerId() {

        List<Item> itemList = itemRepository.findAllByOwnerId(userId, pageable);

        assertNotNull(itemList);
    }

    @Test
    void findItemSearch() {

        String text = "as";

        List<ItemSearch> itemSearch = itemRepository.findItemSearch(text, text, pageable);

        assertEquals(1, itemSearch.size());
    }

    @Test
    void findByIdAndOwnerId() {

        Optional<Item> byIdAndOwnerId = itemRepository.findByIdAndOwnerId(itemId, userId);

        assertTrue(byIdAndOwnerId.isPresent());
    }

    @Test
    void findAllByRequestIds() {
        List<Long> list = List.of(itemRequestId);

        List<Item> allByRequestId = itemRepository.findAllByRequestIds(list);

        assertEquals(allByRequestId.size(), 1);
    }

    @Test
    void findAllByRequestId() {
        List<Item> allByRequestId = itemRepository.findAllByRequestId(itemRequestId);

        assertEquals(allByRequestId.size(), 1);
    }
}
