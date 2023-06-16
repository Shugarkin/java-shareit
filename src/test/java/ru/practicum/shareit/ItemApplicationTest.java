package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemApplicationTest {

    private final ItemService itemService;

//    @Test
//    public void test() {
//
//        Item item = itemService.createItem(1L, Item.builder().name("оружие")
//                .available(true)
//                .description("могучее")
//                .build());
//        Assertions.assertNotNull(item);
//
//        Item newItem = itemService.findItem(1L, 1L);
//        Assertions.assertNotNull(newItem);
//
//        List<Item> listItem = itemService.findAllItemByUser(1L);
//        Assertions.assertNotNull(listItem);
//
//        Item upItem = itemService.updateItem(1L, 1L, Item.builder().name("огромное оружие").build());
//        Assertions.assertNotNull(upItem);
//
//        List<Item> newList = itemService.search(1L, "оружие");
//        Assertions.assertNotNull(newList);
//    }
}
