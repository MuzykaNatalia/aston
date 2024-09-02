package ru.aston.post.service;

import java.util.Collection;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;

public interface PostService {
    Collection<ResponsePostDto> getAllPostsAuthor(long userId);

    ResponsePostDto getPostById(long postId, long userId);

    ResponsePostDto createPost(RequestPostDto post, long userId);

    ResponsePostDto updatePost(RequestPostDto post, long postId, long userId);

    void deletePost(long postId, long userId);
}
