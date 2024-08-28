package ru.aston.user.author.servlet;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import ru.aston.common.AbstractServlet;
import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import ru.aston.user.author.repository.AuthorRepositoryImpl;
import ru.aston.user.author.service.AuthorService;
import ru.aston.user.author.service.AuthorServiceImpl;
import static ru.aston.constant.Constant.HEADER_USER;

public class AuthorServlet extends AbstractServlet {
    private final AuthorService authorService = new AuthorServiceImpl(new AuthorRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        if (headerId == null) {
            Collection<ResponseAuthorDto> users = authorService.getAllUsers();
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, users);
        } else {
            try {
                long userId = Long.parseLong(headerId);
                ResponseAuthorDto user = authorService.getUserById(userId);
                resp.setStatus(HttpServletResponse.SC_OK);
                sendJsonResponse(resp, user);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                try {
                    resp.getWriter().write("Invalid user ID format");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        RequestAuthorDto user = readJsonRequest(req, RequestAuthorDto.class);
        ResponseAuthorDto addedUser = authorService.createUser(user);
        resp.setStatus(HttpServletResponse.SC_OK);
        sendJsonResponse(resp, addedUser);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);
        RequestAuthorDto user = readJsonRequest(req, RequestAuthorDto.class);

        try {
            long userId = Long.parseLong(headerId);
            ResponseAuthorDto updatedUser = authorService.updateUser(userId, user);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, updatedUser);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, "Invalid id format");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        try {
            long userId = Long.parseLong(headerId);
            authorService.deleteUser(userId);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, "User deleted successfully");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, "Invalid id format");
        }
    }
}
