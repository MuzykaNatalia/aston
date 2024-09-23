package ru.aston.post.controller;

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
import ru.aston.config.Create;
import ru.aston.config.Update;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;
import ru.aston.post.service.PostService;

import static ru.aston.constant.Constant.HEADER_USER;
import static ru.aston.constant.Constant.REASON_BAD_REQUEST;
import static ru.aston.constant.Constant.REASON_CREATED;
import static ru.aston.constant.Constant.REASON_INTERNAL_SERVER_ERROR;
import static ru.aston.constant.Constant.REASON_NOT_FOUND;
import static ru.aston.constant.Constant.REASON_OK;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Api(value = "/api/posts", tags = "Posts Controller")
public class PostApiController {
    private final PostService postService;

    @GetMapping
    @ApiOperation(value = "Get all posts for author")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public Collection<ResponsePostDto> getAllPostsAuthor(@RequestHeader(HEADER_USER) @Positive @NotNull
                                                             @ApiParam(name = "ID of the author", required = true)
                                                             Long userId) {
        return postService.getAllPostsAuthor(userId);
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "Get an post by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponsePostDto getPostById(@PathVariable @Positive @NotNull
                                           @ApiParam(name = "ID of the post to retrieve", required = true)
                                           Long postId,
                                       @RequestHeader(HEADER_USER) @Positive @NotNull
                                       @ApiParam(name = "ID of the author", required = true) Long userId) {
        return postService.getPostById(postId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new post")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = REASON_CREATED),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponsePostDto createPost(@Validated(Create.class) @RequestBody @NotNull
                                          @ApiParam(name = "Post object to be created", required = true)
                                          RequestPostDto requestPostDto,
                                      @RequestHeader(HEADER_USER) @Positive @NotNull
                                      @ApiParam(name = "ID of the author", required = true) Long userId) {
        return postService.createPost(requestPostDto, userId);
    }

    @PatchMapping("/{postId}")
    @ApiOperation(value = "Update an existing post")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public ResponsePostDto updatePost(@Validated(Update.class) @RequestBody @NotNull
                                          @ApiParam(name = "Post object to be updated", required = true)
                                          RequestPostDto requestPostDto,
                                      @PathVariable @Positive @NotNull
                                      @ApiParam(name = "ID of the post", required = true) Long postId,
                                      @RequestHeader(HEADER_USER) @Positive @NotNull
                                          @ApiParam(name = "ID of the author", required = true) Long userId) {
        return postService.updatePost(requestPostDto, postId, userId);
    }

    @DeleteMapping("/{postId}")
    @ApiOperation(value = "Delete an existing post")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = REASON_OK),
            @ApiResponse(code = 400, message = REASON_BAD_REQUEST),
            @ApiResponse(code = 404, message = REASON_NOT_FOUND),
            @ApiResponse(code = 500, message = REASON_INTERNAL_SERVER_ERROR)
    })
    public void deletePost(@PathVariable @Positive @NotNull  @ApiParam(name = "ID of the post", required = true)
                               Long postId,
                           @RequestHeader(HEADER_USER) @Positive @NotNull
                           @ApiParam(name = "ID of the author", required = true) Long userId) {
        postService.deletePost(postId, userId);
    }
}
