package ru.practicum.shareit.gateway.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.client.ItemClient;
import ru.practicum.controller.ItemController;
import ru.practicum.dto.CommentDtoReceived;
import ru.practicum.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;


    private long itemId = 1L;

    private long userId = 1L;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    private ItemDto itemDto = ItemDto.builder()
            .id(itemId)
            .name("asd")
            .available(true)
            .description("asd")
            .build();

    @SneakyThrows
    @Test
    void createItem() {

        when(itemClient.createItem(userId, itemDto)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/items", itemDto)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void createItemNotValidItem() {

        itemDto.setDescription(null);

        when(itemClient.createItem(userId, itemDto)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/items", itemDto)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void findItem() {

        when(itemClient.findItem(userId, itemId)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void findItemNotValidId() {

        when(itemClient.findItem(userId, -1)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/items/{itemId}", -1)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void updateItem() {
        ItemDto itemDto1 = itemDto;
        itemDto1.setDescription("изменено");

        when(itemClient.updateItem(userId, itemId, itemDto1)).thenReturn(objectResponseEntity);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto1)))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void updateItemNotValid() {
        ItemDto itemDto1 = itemDto;
        itemDto1.setDescription("");

        when(itemClient.updateItem(userId, itemId, itemDto1)).thenReturn(objectResponseEntity);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto1)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void findAllItemByUser() {

        when(itemClient.findAllItemByUser(userId, 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void findAllItemByUserNotValidUser() {

        when(itemClient.findAllItemByUser(-1, 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", -1)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void search() {
        when(itemClient.search(userId, "asd", 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", "asd")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void searchNotValidUser() {
        when(itemClient.search(-1, "asd", 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", -1)
                        .param("text", "asd")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void createComment() {
        CommentDtoReceived commentDtoReceived = CommentDtoReceived.builder()
                .text("asd")
                .build();

        when(itemClient.createComment(userId, itemId, commentDtoReceived)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentDtoReceived)))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void createCommentNotValidComment() {
        CommentDtoReceived commentDtoReceived = CommentDtoReceived.builder()
                .build();

        when(itemClient.createComment(userId, itemId, commentDtoReceived)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentDtoReceived)))
                .andExpect(status().isBadRequest());

    }
}