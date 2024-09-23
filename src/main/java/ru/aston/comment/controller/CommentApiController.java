package ru.aston.comment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import static ru.aston.constant.Constant.REASON_BAD_REQUEST;
import static ru.aston.constant.Constant.REASON_CREATED;
import static ru.aston.constant.Constant.REASON_INTERNAL_SERVER_ERROR;
import static ru.aston.constant.Constant.REASON_NOT_FOUND;
import static ru.aston.constant.Constant.REASON_OK;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Api(value = "/api/comments", tags = "Comments Controller")
public class CommentApiController {
    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    @ApiOperation(value = "Get all comments for post")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public Collection<ResponseCommentDto> getAllCommentsForPost(@PathVariable @Positive @NotNull
                                                                    @ApiParam(name = "ID of the post", required = true)
                                                                    Long postId,
                                                                @RequestHeader(HEADER_USER) @Positive @NotNull
                                                                @ApiParam(name = "ID of the author", required = true)
                                                                Long userId) {
        return commentService.getAllCommentsForPost(postId, userId);
    }

    @GetMapping("/{commentId}")
    @ApiOperation(value = "Get an comment by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponseCommentDto getCommentById(@PathVariable @Positive @NotNull
                                                 @ApiParam(name = "ID of the comment to retrieve", required = true)
                                                 Long commentId,
                                             @RequestHeader(HEADER_USER) @Positive @NotNull
                                             @ApiParam(name = "ID of the author", required = true) Long userId) {
        return commentService.getCommentById(commentId, userId);
    }

    @PostMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new comment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = REASON_CREATED),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponseCommentDto createComment(@Validated(Create.class) @RequestBody @NotNull
                                                @ApiParam(name = "Comment object to be created", required = true)
                                                RequestCommentDto requestCommentDto,
                                            @PathVariable @Positive @NotNull
                                            @ApiParam(name = "ID of the post", required = true) Long postId,
                                            @RequestHeader(HEADER_USER) @Positive @NotNull
                                                @ApiParam(name = "ID of the author", required = true) Long userId) {
        return commentService.createComment(requestCommentDto, postId, userId);
    }

    @PatchMapping("/{commentId}")
    @ApiOperation(value = "Update an existing comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponseCommentDto updateComment(@Validated(Update.class) @RequestBody @NotNull
                                                @ApiParam(name = "Comment object to be updated", required = true)
                                                RequestCommentDto requestCommentDto,
                                            @PathVariable @Positive @NotNull
                                            @ApiParam(name = "ID of the comment", required = true) Long commentId,
                                            @RequestHeader(HEADER_USER) @Positive @NotNull
                                                @ApiParam(name = "ID of the author", required = true) Long userId) {
        return commentService.updateComment(requestCommentDto, commentId, userId);
    }

    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "Delete an existing comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public void deleteComment(@PathVariable @Positive @NotNull
                                  @ApiParam(name = "ID of the comment to delete", required = true) Long commentId,
                              @RequestHeader(HEADER_USER) @Positive @NotNull
                              @ApiParam(name = "ID of the author", required = true) Long userId) {
        commentService.deleteComment(commentId, userId);
    }
}
