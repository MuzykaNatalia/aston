package ru.aston.comment.repository;

import java.util.Collection;
import ru.aston.comment.entity.Comment;

public interface CommentRepository {
    void delete(long commentId);

    Comment findById(long commentId);

    Comment save(Comment comment);

    Comment update(Comment comment);

    Collection<Comment> findCommentsByPostId(long postId);
}
