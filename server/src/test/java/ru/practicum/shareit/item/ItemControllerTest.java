package ru.practicum.shareit.item;

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

    private long userId = 1L;

    private long itemId = 1L;

    @Test
    void createItem() {
        Item item = Item.builder().build();
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemService.createItem(userId, item)).thenReturn(item);

        Item newItem = ItemMapper.toItem(itemController.createItem(userId, itemDto));

        assertEquals(newItem, item);
    }

    @Test
    void findItem() {
        ItemWithBookingAndComment item = ItemWithBookingAndComment.builder().build();
        List<CommentReceiving> comment = List.of();
        item.setComments(comment);
        ItemDtoWithBookingAndComment itemDto = ItemMapper.itemDtoWithBooking(item);

        when(itemService.findItem(userId, itemId)).thenReturn(item);

        ItemDtoWithBookingAndComment newItem = itemController.findItem(userId, itemId);

        assertEquals(newItem, itemDto);
    }

    @Test
    void updateItem() {
        Item item = Item.builder().name("палка").build();
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemService.updateItem(userId, itemId, item)).thenReturn(item);

        ItemDto newItem = itemController.updateItem(userId, itemId, itemDto);

        assertEquals(newItem, itemDto);
    }

    @Test
    void findAllItemByUser() {
        ItemWithBookingAndComment item = ItemWithBookingAndComment.builder().build();
        List<CommentReceiving> comment = List.of();
        item.addComments(comment);
        List<ItemWithBookingAndComment> itemList = List.of(item);

        List<ItemDtoWithBookingAndComment> itemDto = ItemMapper.toListItemDtoWithBooking(itemList);

        int from = 0;
        int size = 10;

        when(itemService.findAllItemByUser(userId, from, size)).thenReturn(itemList);

        List<ItemDtoWithBookingAndComment> list = itemController.findAllItemByUser(userId, from, size);

        assertEquals(list, itemDto);
    }

    @Test
    void search() {
        String text = "asd";
        int from = 0;
        int size = 10;

        List<ItemSearch> itemList = List.of();

        when(itemService.search(userId, text, from, size)).thenReturn(itemList);

        List<ItemDto> listDto = itemController.search(userId, text, from, size);

        assertEquals(listDto, itemList);
    }

    @Test
    void createComment() {
        Item item = Item.builder().id(1L).build();
        User user = User.builder().name("fdfds").build();

        CommentDtoReceived commentReceiving = CommentDtoReceived.builder().text("asd").build();
        Comment comment = CommentMapper.toCommentFromCommentDtoReceived(commentReceiving);
        comment.setItem(item);
        comment.setUser(user);

        Comment commentToMatch = CommentMapper.toCommentFromCommentDtoReceived(commentReceiving);

        CommentDto commentDtoToMatch = CommentMapper.toCommentDto(comment);

        when(itemService.createComment(userId, itemId, commentToMatch)).thenReturn(comment);
        Comment comment2 = itemService.createComment(userId, itemId, comment);

        CommentDto commentDto = itemController.createComment(userId, itemId, commentReceiving);

        assertEquals(commentDto, commentDtoToMatch);
    }
}
