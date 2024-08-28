package ru.aston.post.servlet;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.aston.common.AbstractServlet;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;
import ru.aston.post.repository.PostRepositoryImpl;
import ru.aston.post.service.PostService;
import ru.aston.post.service.PostServiceImpl;
import static ru.aston.constant.Constant.HEADER_USER;

public class PostServlet extends AbstractServlet {
    private final PostService postService = new PostServiceImpl(new PostRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        try {
            long userId = Long.parseLong(headerId);
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                Collection<ResponsePostDto> posts = postService.getAllPostsAuthor(userId);
                resp.setStatus(HttpServletResponse.SC_OK);
                sendJsonResponse(resp, posts);
            } else {
                long postId = Long.parseLong(pathInfo.substring(1));
                ResponsePostDto post = postService.getPostById(postId, userId);
                resp.setStatus(HttpServletResponse.SC_OK);
                sendJsonResponse(resp, post);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                resp.getWriter().write("Invalid user ID format");
            } catch (IOException ex) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                resp.getWriter().write(e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        try {
            long userId = Long.parseLong(headerId);

            RequestPostDto post = readJsonRequest(req, RequestPostDto.class);
            ResponsePostDto addedPost = postService.createPost(post, userId);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, addedPost);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                resp.getWriter().write("Invalid user ID format");
            } catch (IOException ex) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        try {
            long userId = Long.parseLong(headerId);
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                throw new RuntimeException("Invalid post ID format");
            }

            long postId = Long.parseLong(pathInfo.substring(1));
            RequestPostDto post = readJsonRequest(req, RequestPostDto.class);
            ResponsePostDto updatedPost = postService.updatePost(post, postId, userId);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, updatedPost);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                resp.getWriter().write("Invalid user ID format");
            } catch (IOException ex) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        try {
            long userId = Long.parseLong(headerId);
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                throw new RuntimeException("Invalid post ID format");
            }

            long postId = Long.parseLong(pathInfo.substring(1));
            postService.deletePost(postId, userId);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, "Post deleted successfully");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, "Invalid id format");
        }
    }
}
