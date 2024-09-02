package ru.aston.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static ru.aston.constant.Constant.DB_PASSWORD;
import static ru.aston.constant.Constant.DB_URL;
import static ru.aston.constant.Constant.DB_USER;
import static ru.aston.constant.Constant.DRIVER_NAME;
import static ru.aston.constant.Constant.SCHEMA_SQL_FILE;

public class AbstractServlet extends HttpServlet {
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        try {
            super.init();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        initDatabase();
    }

    public void initDatabase() {
        try {
            Class.forName(DRIVER_NAME);

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement statement = connection.createStatement()) {

                InputStream sqlStream = AbstractServlet.class.getClassLoader().getResourceAsStream(SCHEMA_SQL_FILE);
                if (sqlStream == null) {
                    throw new RuntimeException("Could not find the file to initialize the database");
                }

                String sql = new String(sqlStream.readAllBytes());
                statement.execute(sql);
            }
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T readJsonRequest(HttpServletRequest req, Class<T> clazz) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            return objectMapper.readValue(json.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error reading JSON request", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendJsonResponse(HttpServletResponse resp, int status, Object data) {
        resp.setStatus(status);
        resp.setContentType("application/json");
        try (PrintWriter writer = resp.getWriter()) {
            writer.print(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error writing JSON response", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected long parseId(String id, HttpServletResponse resp) {
        if (id == null || id.isBlank()) {
            sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "ID cannot be null or empty");
            return -1;
        }

        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
            return -1;
        }
    }

    protected void getExceptionIfHeaderEmpty(String headerId, HttpServletResponse resp) {
        if (headerId == null || headerId.isEmpty()) {
            throw new IllegalArgumentException("The header must not be empty");
        }
    }

    protected void getExceptionIfPathEmpty(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new IllegalArgumentException("Invalid request path");
        }
    }
}
