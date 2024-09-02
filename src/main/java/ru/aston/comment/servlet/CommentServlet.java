package ru.aston.comment.servlet;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.aston.comment.dto.RequestCommentDto;
import ru.aston.comment.dto.ResponseCommentDto;
import ru.aston.comment.repository.CommentRepository;
import ru.aston.comment.repository.CommentRepositoryImpl;
import ru.aston.comment.service.CommentService;
import ru.aston.comment.service.CommentServiceImpl;
import ru.aston.common.AbstractServlet;
import ru.aston.post.repository.PostRepository;
import ru.aston.post.repository.PostRepositoryImpl;
import ru.aston.user.author.repository.AuthorRepository;
import ru.aston.user.author.repository.AuthorRepositoryImpl;
import static ru.aston.constant.Constant.HEADER_USER;

public class CommentServlet extends AbstractServlet {
    private final CommentRepository commentRepository = new CommentRepositoryImpl();
    private final PostRepository postRepository = new PostRepositoryImpl();
    private final AuthorRepository authorRepository = new AuthorRepositoryImpl();
    private final CommentService commentService = new CommentServiceImpl(commentRepository, postRepository,
            authorRepository);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long userId = parseId(headerId, resp);
        if (userId < 1) {
            return;
        }

        String pathInfo = req.getPathInfo();
        getExceptionIfPathEmpty(pathInfo);

        long postId = parseId(pathInfo.substring(1), resp);
        if (postId < 1) {
            return;
        }

        String commentIdParam = req.getParameter("commentId");
        if (commentIdParam == null) {
            Collection<ResponseCommentDto> comments = commentService.getAllCommentsForPost(postId, userId);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, comments);
        } else {
            long commentId = parseId(pathInfo.substring(1), resp);
            if (commentId < 1) {
                return;
            }

            ResponseCommentDto comment = commentService.getCommentById(commentId, userId);
            if (comment == null) {
                sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Comment not found");
                return;
            }

            sendJsonResponse(resp, HttpServletResponse.SC_OK, comment);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long ownerCommentId = parseId(headerId, resp);
        if (ownerCommentId < 1) {
            return;
        }

        String pathInfo = req.getPathInfo();
        getExceptionIfPathEmpty(pathInfo);

        long postId = parseId(pathInfo.substring(1), resp);
        if (postId < 1) {
            return;
        }

        RequestCommentDto comment = readJsonRequest(req, RequestCommentDto.class);
        ResponseCommentDto addedComment = commentService.createComment(comment, postId, ownerCommentId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, addedComment);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long ownerCommentId = parseId(headerId, resp);
        if (ownerCommentId < 1) {
            return;
        }

        String pathInfo = req.getPathInfo();
        getExceptionIfPathEmpty(pathInfo);

        long commentId = parseId(pathInfo.substring(1), resp);
        if (commentId < 1) {
            return;
        }

        RequestCommentDto comment = readJsonRequest(req, RequestCommentDto.class);
        ResponseCommentDto updatedPost = commentService.updateComment(comment, commentId, ownerCommentId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, updatedPost);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long ownerCommentId = parseId(headerId, resp);
        if (ownerCommentId < 1) {
            return;
        }

        String pathInfo = req.getPathInfo();
        getExceptionIfPathEmpty(pathInfo);

        long commentId = parseId(pathInfo.substring(1), resp);
        if (commentId < 1) {
            return;
        }

        commentService.deleteComment(commentId, ownerCommentId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, "Comment deleted successfully");
    }
}
