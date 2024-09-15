package ru.aston.comment.mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.aston.comment.dto.RequestCommentDto;
import ru.aston.comment.dto.ResponseCommentDto;
import ru.aston.comment.entity.Comment;
import ru.aston.post.entity.Post;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.mapper.AuthorMapper;
import static ru.aston.constant.Constant.FORMATTER;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final AuthorMapper authorMapper;

    public Comment toComment(RequestCommentDto commentDto, Author author, Post post) {
        return Comment.builder()
                .createdOn(LocalDateTime.now())
                .description(commentDto.getDescription())
                .author(author)
                .post(post)
                .build();
    }

    public ResponseCommentDto toResponseCommentDto(Comment comment) {
        return ResponseCommentDto.builder()
                .createdOn(comment.getCreatedOn().format(FORMATTER))
                .description(comment.getDescription())
                .user(authorMapper.toAuthorDto(comment.getAuthor()))
                .build();
    }

    public Collection<ResponseCommentDto> toResponseCommentDtoCollection(Collection<Comment> comments) {
        return comments.stream().map(this::toResponseCommentDto).collect(Collectors.toSet());
    }
}
