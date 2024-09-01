package ru.aston.comment.repository;

import java.util.Collection;
import ru.aston.comment.model.Comment;

public interface CommentRepository {
    void delete(long commentId);

    Comment findById(long commentId);

    Comment save(Comment comment);

    void update(Comment comment);

    Collection<Comment> findCommentsByPostId(long postId);
}
