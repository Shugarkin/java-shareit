package ru.practicum.shareit.Item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

    //изначально хотел воспользоваться методами beforeEach, afterEach, но из-за присвоения бд id
    // не всегда можно отловить в каком методе какой id будет у сущности

    @Test
    void findAllByOwnerIdTest() {
        long itemId = 4L;
        long userId = 4L;
        long itemRequestId = 4L;
        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        userRepository.save(user);

        ItemRequest itemRequest = ItemRequest.builder()
                .description("qwe").id(itemRequestId).userId(userId).created(LocalDateTime.now()).build();
        itemRequestRepository.save(itemRequest);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").requestId(itemRequestId).owner(user).build();
        itemRepository.save(item);

        List<Item> itemList = itemRepository.findAllByOwnerId(userId);

        assertNotNull(itemList);
    }

    @Test
    void findItemSearchTest() {
        long itemId = 1L;
        long userId = 1L;
        long itemRequestId = 1L;
        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        userRepository.save(user);

        ItemRequest itemRequest = ItemRequest.builder()
                .description("qwe").id(itemRequestId).userId(userId).created(LocalDateTime.now()).build();
        itemRequestRepository.save(itemRequest);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").requestId(itemRequestId).owner(user).build();
        itemRepository.save(item);

        String text = "as";

        List<ItemSearch> itemSearch = itemRepository.findItemSearch(text, text);

        assertEquals(1, itemSearch.size());
    }

    @Test
    void findByIdAndOwnerIdTest() {
        long itemId = 2L;
        long userId = 2L;
        long itemRequestId = 2L;
        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        userRepository.save(user);

        ItemRequest itemRequest = ItemRequest.builder()
                .description("qwe").id(itemRequestId).userId(userId).created(LocalDateTime.now()).build();
        itemRequestRepository.save(itemRequest);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").requestId(itemRequestId).owner(user).build();
        itemRepository.save(item);

        Optional<Item> byIdAndOwnerId = itemRepository.findByIdAndOwnerId(itemId, userId);

        assertTrue(byIdAndOwnerId.isPresent());
    }

    @Test
    void findAllByRequestIdTest() {
        long itemId = 3L;
        long userId = 3L;
        long itemRequestId = 3L;
        User user = User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build();
        userRepository.save(user);

        ItemRequest itemRequest = ItemRequest.builder()
                .description("qwe").id(itemRequestId).userId(userId).created(LocalDateTime.now()).build();
        itemRequestRepository.save(itemRequest);

        Item item = Item.builder()
                .id(itemId).name("asd").available(true).description("asdf").requestId(itemRequestId).owner(user).build();
        itemRepository.save(item);

        List<Long> list = List.of(3L);
        List<Item> allByRequestId = itemRepository.findAllByRequestId(list);

        assertEquals(1, allByRequestId.size());
    }

}
