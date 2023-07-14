package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestWithItems;

import java.util.List;

public interface ItemRequestService {

    ItemRequest addRequest(ItemRequest request, Long userId);

    List<ItemRequestWithItems> findListRequest(long userId, int from, int size);

    List<ItemRequestWithItems> findListRequestUser(long userId);

    ItemRequestWithItems findItemRequest(long userId, long requestId);
}
