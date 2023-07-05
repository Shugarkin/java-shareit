package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void addRequestTest() {
        long userId = 1L;
        User user = User.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ItemRequest request = ItemRequest.builder().build();
        when(itemRequestRepository.save(request)).thenReturn(request);

        ItemRequest itemRequest = itemRequestService.addRequest(request, userId);

        assertNotNull(itemRequest);
    }

    @Test
    void findListRequestTest() {

    }
}
