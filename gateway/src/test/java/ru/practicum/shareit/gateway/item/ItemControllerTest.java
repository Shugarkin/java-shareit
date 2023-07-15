package ru.practicum.shareit.gateway.item;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.client.ItemClient;
import ru.practicum.controller.ItemController;
import ru.practicum.dto.CommentDtoReceived;
import ru.practicum.dto.ItemDto;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    ItemClient itemClient;

    @InjectMocks
    ItemController itemController;

    private long userId = 1L;

    private long itemId = 1L;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @Test
    void createItem() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .description("asd")
                .available(true)
                .name("asdsf")
                .build();

        when(itemClient.createItem(userId, itemDto)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> item = itemController.createItem(userId, itemDto);

        assertEquals(objectResponseEntity, item);
    }

    @Test
    void findItem() {

        when(itemClient.findItem(userId, itemId)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> item = itemController.findItem(userId, itemId);

        assertEquals(objectResponseEntity, item);
    }

    @Test
    void updateItem() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .description("asd")
                .available(true)
                .name("asdsf")
                .build();

        when(itemClient.updateItem(userId, itemId, itemDto)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> objectResponseEntity1 = itemController.updateItem(userId, itemId, itemDto);

        assertEquals(objectResponseEntity1, objectResponseEntity);
    }

    @Test
    void findAllItemByUser() {

        int from = 0;
        int size = 10;

        when(itemClient.findAllItemByUser(userId, from, size)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> allItemByUser = itemController.findAllItemByUser(userId, from, size);

        assertEquals(allItemByUser, objectResponseEntity);
    }

    @Test
    void search() {
        String text = "asd";
        int from = 0;
        int size = 10;

        when(itemClient.search(userId, text, from, size)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> search = itemController.search(userId, text, from, size);

        assertEquals(search, objectResponseEntity);
    }

    @Test
    void createComment() {
        CommentDtoReceived commentReceiving = CommentDtoReceived
                .builder()
                .text("asd")
                .build();

        when(itemClient.createComment(userId, itemId, commentReceiving)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> comment = itemController.createComment(userId, itemId, commentReceiving);

        assertEquals(comment, objectResponseEntity);
    }
}

