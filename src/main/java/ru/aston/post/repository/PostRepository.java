package ru.aston.post.repository;

import java.util.Collection;
import ru.aston.post.model.Post;

public interface PostRepository {
    Collection<Post> getAllPostsUser(long userId);

    Post getPostById(long postId);

    Post createPost(Post post);

    Post updatePost(Post post);

    void deletePost(long postId);
}
