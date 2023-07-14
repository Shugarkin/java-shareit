package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoReceived;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentReceiving;

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

    public Comment fromCommentDtoReceivedToComment(CommentDtoReceived commentDtoReceived) {
        return Comment.builder()
                .text(commentDtoReceived.getText())
                .build();
    }

    public CommentReceiving fromCommentToCommentReceiving(Comment comment) {
        return CommentReceiving.builder()
                .id(comment.getId())
                .authorName(comment.getUser().getName())
                .text(comment.getText())
                .created(comment.getCreate())
                .item(comment.getItem().getId())
                .build();
    }

    public List<CommentReceiving> toListCommentReceiving(List<Comment> list) {
        return list.stream().map(CommentMapper::fromCommentToCommentReceiving).collect(Collectors.toList());
    }

    public CommentDto fromCommentRecrivingToCommentDto(CommentReceiving comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthorName())
                .text(comment.getText())
                .created(comment.getCreated())
                .item(comment.getItem())
                .build();
    }

    public List<CommentDto> commentDtoList(List<CommentReceiving> list) {
        return list.stream().map(CommentMapper:: fromCommentRecrivingToCommentDto).collect(Collectors.toList());
    }


    public Comment toCommentFromCommentDtoReceived(CommentDtoReceived comment) {
        return Comment.builder()
                .text(comment.getText())
                .build();
    }
}
