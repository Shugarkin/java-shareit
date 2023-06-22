package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoReceived;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getUser().getName())
                .text(comment.getText())
                .created(comment.getCreate())
                .item(comment.getItem().getId())
                .build();
    }

    public List<CommentDto> toListDto(List<Comment> list) {
        return list.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    public Comment toComment(CommentDto comment) {
        return Comment.builder()
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }

    public Comment fromCommentDtoReceivedToComment(CommentDtoReceived commentDtoReceived) {
        return Comment.builder()
                .text(commentDtoReceived.getText())
                .build();
    }
}
