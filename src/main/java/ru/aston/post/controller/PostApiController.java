package ru.aston.post.controller;

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

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;

    @GetMapping
    public Collection<ResponsePostDto> getAllPostsAuthor(@RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return postService.getAllPostsAuthor(userId);
    }

    @GetMapping("/{postId}")
    public ResponsePostDto getPostById(@PathVariable @Positive @NotNull Long postId,
                                       @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return postService.getPostById(postId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponsePostDto createPost(@Validated(Create.class) @RequestBody @NotNull RequestPostDto requestPostDto,
                                      @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return postService.createPost(requestPostDto, userId);
    }

    @PatchMapping("/{postId}")
    public ResponsePostDto updatePost(@Validated(Update.class) @RequestBody @NotNull RequestPostDto requestPostDto,
                                      @PathVariable @Positive @NotNull Long postId,
                                      @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        return postService.updatePost(requestPostDto, postId, userId);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable @Positive @NotNull Long postId,
                           @RequestHeader(HEADER_USER) @Positive @NotNull Long userId) {
        postService.deletePost(postId, userId);
    }
}
