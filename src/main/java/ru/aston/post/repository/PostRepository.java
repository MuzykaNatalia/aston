package ru.aston.post.repository;

import java.util.Collection;
import ru.aston.post.entity.Post;

public interface PostRepository {
    Collection<Post> getAllPostsAuthor(long userId);

    Post getPostById(long postId);

    Post createPost(Post post);

    Post updatePost(Post post);

    void deletePost(Post post);
}
