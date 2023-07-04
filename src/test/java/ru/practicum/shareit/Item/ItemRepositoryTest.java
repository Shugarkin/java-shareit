package ru.practicum.shareit.Item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;
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
            .id(itemId).name("asd").available(true).description("asdf").owner(user).build();

    private ItemRequest itemRequest = ItemRequest.builder()
            .description("qwe").id(itemRequestId).userId(userId).created(LocalDateTime.now().withNano(0)).build();
    @BeforeEach
    void before() {
        user = userRepository.save(user);
        userId =user.getId();

        item.setOwner(user);
        item = itemRepository.save(item);
        itemId = item.getId();

        itemRequest.setUserId(userId);
        itemRequest = itemRequestRepository.save(itemRequest);
    }

    @AfterEach
    void after() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findAllByOwnerIdTest() {

        List<Item> itemList = itemRepository.findAllByOwnerId(userId, pageable);

        assertNotNull(itemList);
    }

    @Test
    void findItemSearchTest() {

        String text = "as";

        List<ItemSearch> itemSearch = itemRepository.findItemSearch(text, text, pageable);

        assertEquals(1, itemSearch.size());
    }

    @Test
    void findByIdAndOwnerIdTest() {

        Optional<Item> byIdAndOwnerId = itemRepository.findByIdAndOwnerId(itemId, userId);

        assertTrue(byIdAndOwnerId.isPresent());
    }

    @Test
    void findAllByRequestIdTest() {

        List<Long> list = List.of(1L);
        List<Item> allByRequestId = itemRepository.findAllByRequestIds(list);

        assertNotEmpty(allByRequestId, "не пуст");
    }

}
