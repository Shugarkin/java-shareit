package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestSearch;
import ru.practicum.shareit.request.model.ItemRequestWithItems;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void addRequest() {
        long userId = 1L;
        User user = User.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ItemRequest request = ItemRequest.builder().build();
        when(itemRequestRepository.save(request)).thenReturn(request);

        ItemRequest itemRequest = itemRequestService.addRequest(request, userId);

        assertNotNull(itemRequest);
    }

    @Test
    void findListRequest() {
        long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("created").ascending());

        List<ItemRequestSearch> list = List.of(new ItemRequestSearch());

        when(itemRequestRepository.findAllByUserIdNotOrderByCreated(userId, pageable)).thenReturn(list);

        when(userRepository.existsById(userId)).thenReturn(true);

        when(itemRepository.findAllByRequestIds(any())).thenReturn(List.of());

        List<ItemRequestWithItems> listRequest = itemRequestService.findListRequest(userId, 0, 10);

        assertNotEmpty(listRequest, "не пуст");
    }

    @Test
    void findListRequestUser() {
        long userId = 1L;

        List<ItemRequestSearch> list = List.of(new ItemRequestSearch());

        when(itemRequestRepository.findAllByUserId(userId)).thenReturn(list);

        when(userRepository.existsById(userId)).thenReturn(true);

        when(itemRepository.findAllByRequestIds(any())).thenReturn(List.of());

        List<ItemRequestWithItems> listRequest = itemRequestService.findListRequestUser(userId);

        assertNotEmpty(listRequest, "не пуст");
    }

    @Test
    void findItemRequest() {
        long itemRequestId = 1L;
        long userId = 1L;
        ItemRequestSearch item = new ItemRequestSearch();

        when(userRepository.existsById(userId)).thenReturn(true);

        when(itemRequestRepository.findById(itemRequestId)).thenReturn(Optional.of(item));

        when(itemRepository.findAllByRequestId(itemRequestId)).thenReturn(List.of());

        ItemRequestWithItems itemRequest = itemRequestService.findItemRequest(userId, itemRequestId);

        assertNotNull(itemRequest);
    }

    @Test
    void findItemRequestNotValid() {
        long itemRequestId = 1L;
        long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () ->  itemRequestService.findItemRequest(userId, itemRequestId));
    }
}
