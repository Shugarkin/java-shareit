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
import ru.practicum.shareit.request.model.ItemRequestSearch;
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

    @Test
    void addRequestTest() {
        long userId = 1L;
        ItemRequestDtoReceived itemRequestDtoReceived = ItemRequestDtoReceived.builder().build();
        ItemRequest itemRequest = ItemRequest.builder().build();
        ItemRequestDto itemRequestDto = RequestMapper.toRequestDto(itemRequest);

        when(itemRequestService.addRequest(any(), any())).thenReturn(itemRequest);

        ItemRequestDto itemRequestDtoNew = itemRequestController.addRequest(userId, itemRequestDtoReceived);
        assertEquals(itemRequestDto, itemRequestDtoNew);
    }

    @Test
    void findListRequestUserTest() {
        long userId = 1L;

        List<ItemRequestWithItems> list = List.of(ItemRequestWithItems.builder()
                        .items(List.of())
                        .id(1L)
                        .created(LocalDateTime.now())
                        .description("sdsaf")
                        .build());

        when(itemRequestService.findListRequestUser(userId)).thenReturn(list);

        List<ItemRequestWithItemsDto> listRequestUser = RequestMapper.toListRequestWithItemsDto(list);

        List<ItemRequestWithItemsDto> listRequestUserNew = itemRequestController.findListRequestUser(userId);

        assertEquals(listRequestUser, listRequestUserNew);
    }

    @Test
    void findListRequestTest() {
        long userId = 1L;
        int from = 1;
        int size = 10;

        List<ItemRequestWithItems> list = List.of(ItemRequestWithItems.builder()
                .items(List.of())
                .id(1L)
                .created(LocalDateTime.now())
                .description("sdsaf")
                .build());

        when(itemRequestService.findListRequest(userId, from, size)).thenReturn(list);

        List<ItemRequestWithItemsDto> listRequestUser = RequestMapper.toListRequestWithItemsDto(list);

        List<ItemRequestWithItemsDto> listRequestUserNew = itemRequestController.findListRequest(userId, from, size);

        assertEquals(listRequestUser, listRequestUserNew);
    }

    @Test
    void findItemRequestTest() {
        long userId = 1L;
        long itemId = 1L;

        ItemRequestWithItems items = ItemRequestWithItems.builder()
                .items(List.of())
                .id(1L)
                .created(LocalDateTime.now())
                .description("sdsaf")
                .build();

        when(itemRequestService.findItemRequest(userId, itemId)).thenReturn(items);

        ItemRequestWithItemsDto itemRequest = RequestMapper.toRequestWithItemsDto(items);

        ItemRequestWithItemsDto itemRequestNew = itemRequestController.findItemRequest(userId, itemId);

        assertEquals(itemRequest, itemRequestNew);
    }
}
