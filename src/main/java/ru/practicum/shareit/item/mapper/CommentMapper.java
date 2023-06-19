package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .user(comment.getUser().getId())
                .text(comment.getText())
                .create(comment.getCreate())
                .item(comment.getItem().getId())
                .build();
    }

    public List<CommentDto> toListDto(List<Comment> list) {
        return list.stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }
}
