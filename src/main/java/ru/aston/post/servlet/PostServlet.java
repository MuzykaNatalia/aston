package ru.aston.post.servlet;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.aston.common.AbstractServlet;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;
import ru.aston.post.repository.PostRepository;
import ru.aston.post.repository.PostRepositoryImpl;
import ru.aston.post.service.PostService;
import ru.aston.post.service.PostServiceImpl;
import ru.aston.user.repository.UserRepository;
import ru.aston.user.repository.UserRepositoryImpl;
import static ru.aston.constant.Constant.HEADER_USER;

public class PostServlet extends AbstractServlet {
    private final PostRepository postRepository = new PostRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final PostService postService = new PostServiceImpl(postRepository, userRepository);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long userId = parseId(headerId, resp);
        if (userId < 1) {
            return;
        }

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            Collection<ResponsePostDto> posts = postService.getAllPostsUser(userId);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, posts);
        } else {
            long postId = parseId(pathInfo.substring(1), resp);
            if (postId < 1) {
                return;
            }

            ResponsePostDto post = postService.getPostById(postId, userId);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, post);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long userId = parseId(headerId, resp);
        if (userId < 1) {
            return;
        }

        RequestPostDto post = readJsonRequest(req, RequestPostDto.class);
        ResponsePostDto addedPost = postService.createPost(post, userId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, addedPost);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
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

        RequestPostDto post = readJsonRequest(req, RequestPostDto.class);
        ResponsePostDto updatedPost = postService.updatePost(post, postId, userId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, updatedPost);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
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

        postService.deletePost(postId, userId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, "Post deleted successfully");
    }
}
