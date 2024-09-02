package ru.aston.user.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ru.aston.user.model.User;
import ru.aston.user.repository.mapper.RowMapper;
import static ru.aston.constant.Constant.DB_PASSWORD;
import static ru.aston.constant.Constant.DB_URL;
import static ru.aston.constant.Constant.DB_USER;

public class CustomUserJdbcTemplate {
    public List<User> query(String sql, RowMapper<User> rowMapper, List<Object> params) {
        List<User> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public User insert(String sql, RowMapper<User> rowMapper, List<Object> params) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                } else {
                    throw new SQLException("No generated key found");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(String sql, List<Object> params) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
        }
    }
}
