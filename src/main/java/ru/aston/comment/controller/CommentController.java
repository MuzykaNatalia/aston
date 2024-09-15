package ru.aston.comment.controller;

import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.comment.dto.RequestCommentDto;
import ru.aston.comment.dto.ResponseCommentDto;
import ru.aston.comment.service.CommentService;
import ru.aston.config.Create;
import ru.aston.config.Update;
import static ru.aston.constant.Constant.HEADER_USER;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public Collection<ResponseCommentDto> getAllCommentsForPost(@PathVariable @Positive @NotNull Long postId,
                                                                @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return commentService.getAllCommentsForPost(postId, userId);
    }

    @GetMapping("/{commentId}")
    public ResponseCommentDto getCommentById(@PathVariable @Positive @NotNull Long commentId,
                                             @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return commentService.getCommentById(commentId, userId);
    }

    @PostMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCommentDto createComment(@Validated(Create.class) @RequestBody @NotNull RequestCommentDto requestCommentDto,
                                            @PathVariable @Positive @NotNull Long postId,
                                            @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return commentService.createComment(requestCommentDto, postId, userId);
    }

    @PatchMapping("/{commentId}")
    public ResponseCommentDto updateComment(@Validated(Update.class) @RequestBody @NotNull RequestCommentDto requestCommentDto,
                                            @PathVariable @Positive @NotNull Long commentId,
                                            @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return commentService.updateComment(requestCommentDto, commentId, userId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable @Positive @NotNull Long commentId,
                              @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        commentService.deleteComment(commentId, userId);
    }
}
