package ru.aston.user.servlet;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.aston.common.AbstractServlet;
import ru.aston.user.dto.RequestUserDto;
import ru.aston.user.dto.ResponseUserDto;
import ru.aston.user.repository.UserRepository;
import ru.aston.user.repository.UserRepositoryImpl;
import ru.aston.user.service.UserService;
import ru.aston.user.service.UserServiceImpl;
import static ru.aston.constant.Constant.HEADER_USER;

public class UserServlet extends AbstractServlet {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userRepository);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        if (headerId == null) {
            Collection<ResponseUserDto> users = userService.getAllUsers();
            sendJsonResponse(resp, HttpServletResponse.SC_OK, users);
        } else {
            long userId = parseId(headerId, resp);
            if (userId < 1) {
                return;
            }

            ResponseUserDto user = userService.getUserById(userId);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        RequestUserDto user = super.readJsonRequest(req, RequestUserDto.class);
        ResponseUserDto addedUser = userService.createUser(user);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, addedUser);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);
        RequestUserDto user = readJsonRequest(req, RequestUserDto.class);

        long userId = parseId(headerId, resp);
        if (userId < 1) {
            return;
        }

        ResponseUserDto updatedUser = userService.updateUser(userId, user);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, updatedUser);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String headerId = req.getHeader(HEADER_USER);
        getExceptionIfHeaderEmpty(headerId, resp);

        long userId = parseId(headerId, resp);
        if (userId < 1) {
            return;
        }

        userService.deleteUser(userId);
        sendJsonResponse(resp, HttpServletResponse.SC_OK, "User deleted successfully");
    }
}
