package ru.aston.comment.mapper;

import java.time.LocalDateTime;
import ru.aston.comment.dto.RequestCommentDto;
import ru.aston.comment.dto.ResponseCommentDto;
import ru.aston.comment.model.Comment;
import ru.aston.user.model.User;
import ru.aston.user.service.mapper.UserMapper;
import static ru.aston.constant.Constant.FORMATTER;

public class CommentMapper {
    private final UserMapper userMapper = new UserMapper();

    public Comment toComment(RequestCommentDto commentDto, long userId, long postId) {
        return Comment.builder()
                .createdOn(LocalDateTime.now())
                .description(commentDto.getDescription())
                .userId(userId)
                .postId(postId)
                .build();
    }

    public ResponseCommentDto toResponseCommentDto(Comment comment, User user) {
        return ResponseCommentDto.builder()
                .createdOn(comment.getCreatedOn().format(FORMATTER))
                .description(comment.getDescription())
                .user(userMapper.toUserDto(user))
                .build();
    }
}
