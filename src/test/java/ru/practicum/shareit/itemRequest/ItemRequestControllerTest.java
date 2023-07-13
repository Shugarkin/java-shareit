package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReceived;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;


    private ItemRequestWithItems item = ItemRequestWithItems.builder()
            .items(List.of())
            .id(1L)
            .created(LocalDateTime.now())
            .description("sdsaf")
            .build();


    private long userId = 1L;
    private long itemId = 1L;

    @Test
    void addRequest() {
        ItemRequestDtoReceived itemRequestDtoReceived = ItemRequestDtoReceived.builder().build();
        ItemRequest itemRequest = ItemRequest.builder().build();
        ItemRequestDto itemRequestDto = RequestMapper.toRequestDto(itemRequest);

        when(itemRequestService.addRequest(any(), any())).thenReturn(itemRequest);

        ItemRequestDto itemRequestDtoNew = itemRequestController.addRequest(userId, itemRequestDtoReceived);
        assertEquals(itemRequestDto, itemRequestDtoNew);
    }

    @Test
    void findListRequestUser() {

        List<ItemRequestWithItems> list = List.of(item);

        when(itemRequestService.findListRequestUser(userId)).thenReturn(list);

        List<ItemRequestWithItemsDto> listRequestUser = RequestMapper.toListRequestWithItemsDto(list);

        List<ItemRequestWithItemsDto> listRequestUserNew = itemRequestController.findListRequestUser(userId);

        assertEquals(listRequestUser, listRequestUserNew);
    }

    @Test
    void findListRequest() {
        int from = 1;
        int size = 10;

        List<ItemRequestWithItems> list = List.of(item);

        when(itemRequestService.findListRequest(userId, from, size)).thenReturn(list);

        List<ItemRequestWithItemsDto> listRequestUser = RequestMapper.toListRequestWithItemsDto(list);

        List<ItemRequestWithItemsDto> listRequestUserNew = itemRequestController.findListRequest(userId, from, size);

        assertEquals(listRequestUser, listRequestUserNew);
    }

    @Test
    void findItemRequest() {

        when(itemRequestService.findItemRequest(userId, itemId)).thenReturn(item);

        ItemRequestWithItemsDto itemRequest = RequestMapper.toRequestWithItemsDto(item);

        ItemRequestWithItemsDto itemRequestNew = itemRequestController.findItemRequest(userId, itemId);

        assertEquals(itemRequest, itemRequestNew);
    }
}
