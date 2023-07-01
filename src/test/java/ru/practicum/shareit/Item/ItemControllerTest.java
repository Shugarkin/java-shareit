package ru.practicum.shareit.Item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentReceiving;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingAndComment;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    ItemService itemService;

    @InjectMocks
    ItemController itemController;

    @Test
    void createItemTest() {
        long userId = 1L;
        Item item = Item.builder().build();
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemService.createItem(userId, item)).thenReturn(item);

        Item newItem = ItemMapper.toItem(itemController.createItem(userId, itemDto));

        assertEquals(newItem, item);
    }

    @Test
    void findItemTest() {
        long userId = 1L;
        long itemId = 1L;
        ItemWithBookingAndComment item = ItemWithBookingAndComment.builder().build();
        List<CommentReceiving> comment = List.of();
        item.setComments(comment);
        ItemDtoWithBookingAndComment itemDto = ItemMapper.itemDtoWithBooking(item);

        when(itemService.findItem(userId, itemId)).thenReturn(item);

        ItemDtoWithBookingAndComment newItem = itemController.findItem(userId, itemId);

        assertEquals(newItem, itemDto);
    }

    @Test
    void updateItemTest() {
        long itemId = 1L;
        long userId = 1L;
        Item item = Item.builder().name("палка").build();
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemService.updateItem(userId, itemId, item)).thenReturn(item);

        ItemDto newItem = itemController.updateItem(userId, itemId, itemDto);

        assertEquals(newItem, itemDto);
    }

    @Test
    void findAllItemByUserTest() {
        long userId = 1L;
        ItemWithBookingAndComment item = ItemWithBookingAndComment.builder().build();
        List<CommentReceiving> comment = List.of();
        item.addComments(comment);
        List<ItemWithBookingAndComment> itemList = List.of(item);

        List<ItemDtoWithBookingAndComment> itemDto = ItemMapper.toListItemDtoWithBooking(itemList);


        when(itemService.findAllItemByUser(userId)).thenReturn(itemList);

        List<ItemDtoWithBookingAndComment> list = itemController.findAllItemByUser(userId);

        assertEquals(list, itemDto);
    }

    @Test
    void searchTest() {
        long userId = 1L;
        String text = "asd";

        List<ItemSearch> itemList = List.of();

        when(itemService.search(userId, text)).thenReturn(itemList);

        List<ItemDto> listDto = itemController.search(userId, text);

        assertEquals(listDto, itemList);
    }

    @Test
    void createCommentTest() {
        long userId = 1L;
        long itemId = 1L;
        Item item = Item.builder().id(1L).build();
        User user = User.builder().name("fdfds").build();

        CommentDtoReceived commentReceiving = CommentDtoReceived.builder().text("asd").build();
        Comment comment = CommentMapper.a(commentReceiving);
        comment.setItem(item);
        comment.setUser(user);

        Comment commentToMatch = CommentMapper.a(commentReceiving);

        CommentDto commentDtoToMatch = CommentMapper.toCommentDto(comment);

        when(itemService.createComment(userId, itemId, commentToMatch)).thenReturn(comment);
        Comment comment2 = itemService.createComment(userId, itemId, comment);

        CommentDto commentDto = itemController.createComment(userId, itemId, commentReceiving);

        assertEquals(commentDto, commentDtoToMatch);
    }
}
