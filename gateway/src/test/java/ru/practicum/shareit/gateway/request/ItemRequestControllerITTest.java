package ru.practicum.shareit.gateway.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.client.ItemRequestClient;
import ru.practicum.controller.ItemRequestController;
import ru.practicum.dto.ItemRequestDtoReceived;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    private long userId = 1L;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    private ItemRequestDtoReceived itemDto = ItemRequestDtoReceived.builder()
            .description("asd")
            .build();

    @SneakyThrows
    @Test
    void addRequest() {
        when(itemRequestClient.addRequest(userId, itemDto)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/requests", itemDto)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void addRequestNotValid() {
        itemDto.setDescription("");
        when(itemRequestClient.addRequest(userId, itemDto)).thenReturn(objectResponseEntity);

        mockMvc.perform(post("/requests", itemDto)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void findListRequestUser() {
        when(itemRequestClient.findListRequestUser(userId)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/requests")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findListRequest() {
        when(itemRequestClient.findListRequest(userId, 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findListRequestNotValidUser() {
        when(itemRequestClient.findListRequest(-1, 0, 10)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", -1)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void findItemRequest() {
        when(itemRequestClient.findItemRequest(userId, 1)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/requests/{requestId}", 1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());


    }

    @SneakyThrows
    @Test
    void findItemRequestNotValidRequest() {
        when(itemRequestClient.findItemRequest(userId, -1)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/requests/{requestId}", -1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());


    }
}