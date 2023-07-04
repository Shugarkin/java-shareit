package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestSearch;
import ru.practicum.shareit.request.model.ItemRequestWithItems;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequest addRequest(ItemRequest request, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        request.setUserId(userId);
        request.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(request);
    }

    @Override
    public List<ItemRequestWithItems> findListRequest(long userId, int from, int size) {
        checkUser(userId);

        Pageable pageable = PageRequest.of(from, size);

        List<ItemRequestWithItems> listRequestWithItems = RequestMapper
            .toListItemRequestWithItemsFromItemRequestSearch(itemRequestRepository.findAllByUserIdNotOrderByCreated(userId, pageable));

        List<Long> listRequestId = listRequestWithItems.stream().map(ItemRequestWithItems::getId).collect(Collectors.toList());

        Map<Long, List<Item>> mapItem = itemRepository.findAllByRequestIds(listRequestId).stream()
                .collect(Collectors.groupingBy(item -> item.getRequestId(), Collectors.toList()));

        listRequestWithItems.stream()
                .forEach(request -> {
                    List<Item> listItem = mapItem.getOrDefault(request.getId(), List.of());

                    request.addItems(listItem);
                });

        return listRequestWithItems;

    }

    @Override
    public List<ItemRequestWithItems> findListRequestUser(long userId) {
        checkUser(userId);

        List<ItemRequestWithItems> listRequestWithItems =
                RequestMapper.toListItemRequestWithItemsFromItemRequestSearch(itemRequestRepository.findAllByUserId(userId));

        List<Long> listRequestId = listRequestWithItems.stream().map(ItemRequestWithItems::getId).collect(Collectors.toList());

        Map<Long, List<Item>> mapItem = itemRepository.findAllByRequestIds(listRequestId).stream()
                .collect(Collectors.groupingBy(item -> item.getRequestId(), Collectors.toList()));

        listRequestWithItems.stream()
                .forEach(request -> {
                    List<Item> listItem = mapItem.getOrDefault(request.getId(), List.of());

                    request.addItems(listItem);
                });

        return listRequestWithItems;
    }

    @Override
    public ItemRequestWithItems findItemRequest(long userId, long requestId) {
        checkUser(userId);

        ItemRequestWithItems request = RequestMapper
                .toItemRequestWithItemsFromItemRequestSearch(itemRequestRepository.findById(requestId)
                .orElseThrow(() ->new EntityNotFoundException("Нет запроса с данный айди")));

        List<Item> listItem = itemRepository.findAllByRequestId(requestId);

        request.addItems(listItem);
        return request;
    }

    private void checkUser(long userId) {
        boolean answer = userRepository.existsById(userId);
        if (!answer) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
    }

}
