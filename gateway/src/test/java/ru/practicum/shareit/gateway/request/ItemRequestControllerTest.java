package ru.practicum.shareit.gateway.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.client.ItemRequestClient;
import ru.practicum.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoReceived;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestClient itemRequestClient;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private long userId = 1L;
    private long itemId = 1L;

    private ItemRequestDtoReceived request = ItemRequestDtoReceived.builder()
            .userId(userId)
            .description("sdsaf")
            .build();

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    @Test
    void addRequest() {

        when(itemRequestClient.addRequest(any(), any())).thenReturn(objectResponseEntity);

        ResponseEntity<Object> objectResponseEntity1 = itemRequestController.addRequest(userId, request);

        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    void findListRequestUser() {


        when(itemRequestClient.findListRequestUser(userId)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> listRequestUser = itemRequestController.findListRequestUser(userId);

        assertEquals(listRequestUser, objectResponseEntity);
    }

    @Test
    void findListRequest() {
        int from = 1;
        int size = 10;

        when(itemRequestClient.findListRequest(userId, from, size)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> listRequest = itemRequestController.findListRequest(userId, from, size);

        assertEquals(listRequest, objectResponseEntity);
    }

    @Test
    void findItemRequest() {

        when(itemRequestClient.findItemRequest(userId, itemId)).thenReturn(objectResponseEntity);

        ResponseEntity<Object> itemRequest = itemRequestController.findItemRequest(userId, itemId);

        assertEquals(objectResponseEntity, itemRequest);
    }
}

