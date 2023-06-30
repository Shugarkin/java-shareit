package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestWithItems;
import ru.practicum.shareit.user.dao.UserRepository;

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
    public ItemRequest addRequest(ItemRequest request, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        request.setUserId(userId);
        request.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(request);
    }

    @Override
    public List<ItemRequestWithItems> findListRequest(long userId) {
        List<ItemRequestWithItems> listRequestWithItems = RequestMapper.toListItemRequestWithItemsFromItemRequestSearch(itemRequestRepository.findAllByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));

        List<String> listRequestId = listRequestWithItems.stream().map(ItemRequestWithItems::getId).map(Objects::toString).collect(Collectors.toList());
        String query = String.join(",", listRequestId);

        Map<Long, List<Item>> mapItem = itemRepository.findAllByRequestId(query).stream()
                .collect(Collectors.groupingBy(item -> item.getRequestId(), Collectors.toList()));

        listRequestWithItems.stream()
                .forEach(request -> {
                    List<Item> listItem = mapItem.getOrDefault(request.getId(), List.of());

                    request.addItems(listItem);
                });

        return listRequestWithItems;
    }


}
