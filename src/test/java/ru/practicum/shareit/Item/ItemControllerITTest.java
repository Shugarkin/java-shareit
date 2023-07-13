package ru.practicum.shareit.Item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.ItemService.ItemService;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSearch;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingAndComment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private long userId = 1L;

    private long itemId = 1L;
    private Item item = Item.builder().name("asd").available(true).description("asdf").build();

    @SneakyThrows
    @Test
    void createItem() {

        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemService.createItem(userId, item)).thenReturn(item);

        String itemString = mockMvc.perform(post("/items", itemDto)
                .contentType("application/json")
                .header("X-Sharer-User-Id", userId)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(itemString, objectMapper.writeValueAsString(itemDto));
    }

    @SneakyThrows
    @Test
    void createItemNotValid() {
        Item item = Item.builder().name("asd").available(true).build();
        Item item1 = Item.builder().name("asd").description("asdf").build();
        Item item2 = Item.builder().available(true).description("asdf").build();

        ItemDto itemDto = ItemMapper.toItemDto(item);
        ItemDto itemDto1 = ItemMapper.toItemDto(item1);
        ItemDto itemDto2 = ItemMapper.toItemDto(item2);

        when(itemService.createItem(userId, item)).thenReturn(item);

        mockMvc.perform(post("/items", itemDto)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        when(itemService.createItem(userId, item)).thenReturn(item1);

        mockMvc.perform(post("/items", itemDto1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(item1)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        when(itemService.createItem(userId, item)).thenReturn(item2);

        mockMvc.perform(post("/items", itemDto2)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(item2)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).createItem(userId, item);
        verify(itemService, never()).createItem(userId, item1);
        verify(itemService, never()).createItem(userId, item2);
    }

    @SneakyThrows
    @Test
    void findItem() {
        ItemWithBookingAndComment item = ItemWithBookingAndComment.builder()
                .id(itemId).name("asd").available(true).description("asdf").lastBooking(null).nextBooking(null).comments(List.of()).build();


        when(itemService.findItem(userId, itemId)).thenReturn(item);

        mockMvc.perform(get("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        Mockito.verify(itemService, times(1)).findItem(userId, itemId);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemService.updateItem(userId, itemId, item)).thenReturn(item);

        String itemString = mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

       assertEquals(itemString, objectMapper.writeValueAsString(itemDto));
    }

    @SneakyThrows
    @Test
    void findAllItemByUser() {
        List<ItemWithBookingAndComment> itemList =  List.of();

        int from = 0;
        int size = 10;

        when(itemService.findAllItemByUser(userId, from, size)).thenReturn(itemList);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService).findAllItemByUser(userId, from, size);
    }

    @SneakyThrows
    @Test
    void search() {
        String text = "asd";

        int from = 0;
        int size = 10;

        List<ItemSearch> list = List.of();
        when(itemService.search(userId, text, from, size)).thenReturn(list);

        mockMvc.perform(get("/items/search")
                .header("X-Sharer-User-Id", userId)
                .param("text", text))
                .andExpect(status().isOk());
        verify(itemService).search(userId, text, from, size);
    }

    @SneakyThrows
    @Test
    void createComment() {
        Comment comment = Comment.builder()
                .id(1L)
                .create(LocalDateTime.now().withNano(0))
                .user(User.builder().id(userId).name("dgs").email("fdsjnfj@mail.com").build())
                .item(Item.builder()
                        .id(itemId).name("asd").available(true).description("asdf").build())
                .text("asd")
                .build();

        Comment commentToMatch = Comment.builder().text("asd").build();

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        when(itemService.createComment(userId, itemId, commentToMatch)).thenReturn(comment);

        String commentString = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(comment)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        assertEquals(commentString, objectMapper.writeValueAsString(commentDto));
    }
}
