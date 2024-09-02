package ru.aston.comment.service;

import java.util.Collection;
import ru.aston.comment.dto.RequestCommentDto;
import ru.aston.comment.dto.ResponseCommentDto;

public interface CommentService {
    Collection<ResponseCommentDto> getAllCommentsForPost(long postId, long userId);

    ResponseCommentDto getCommentById(long commentId, long userId);

    ResponseCommentDto createComment(RequestCommentDto comment, long postId, long ownerCommentId);

    ResponseCommentDto updateComment(RequestCommentDto comment, long commentId, long ownerCommentId);

    void deleteComment(long commentId, long ownerCommentId);
}
