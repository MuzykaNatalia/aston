package ru.aston.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import ru.aston.dto.RequestUserDto;
import ru.aston.dto.ResponseUserDto;
import ru.aston.repository.UserRepositoryImpl;
import ru.aston.service.UserService;
import ru.aston.service.UserServiceImpl;

public class UserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String HEADER_USER = "X-User-Id";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String idHeader = req.getHeader(HEADER_USER);
        if (idHeader == null) {
            Collection<ResponseUserDto> users = userService.getAllUsers();
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, users);
        } else {
            try {
                long idUser = Long.parseLong(idHeader);
                ResponseUserDto user = userService.getUserById(idUser);
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
        RequestUserDto user = readJsonRequest(req);
        ResponseUserDto addedUser = userService.createUser(user);
        resp.setStatus(HttpServletResponse.SC_OK);
        sendJsonResponse(resp, addedUser);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        getExceptionIfHeaderEmpty(req, resp);
        RequestUserDto user = readJsonRequest(req);

        try {
            long idUser = Long.parseLong(req.getHeader(HEADER_USER));
            ResponseUserDto updatedUser = userService.updateUser(idUser, user);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, updatedUser);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, "Invalid id format");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        getExceptionIfHeaderEmpty(req, resp);

        try {
            long idUser = Long.parseLong(req.getHeader(HEADER_USER));
            userService.deleteUser(idUser);
            resp.setStatus(HttpServletResponse.SC_OK);
            sendJsonResponse(resp, "User deleted successfully");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, "Invalid id format");
        }
    }

    private void getExceptionIfHeaderEmpty(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getHeader(HEADER_USER);
        if (id == null || id.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendJsonResponse(resp, "Id is required");
        }
    }

    private RequestUserDto readJsonRequest(HttpServletRequest req) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            return objectMapper.readValue(json.toString(), RequestUserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error reading JSON request", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendJsonResponse(HttpServletResponse resp, Object data) {
        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            writer.print(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error writing JSON response", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
