package ru.aston.user.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import ru.aston.user.model.User;
import ru.aston.user.repository.mapper.UserRowMapper;
import static ru.aston.constant.Constant.DB_PASSWORD;
import static ru.aston.constant.Constant.DB_URL;
import static ru.aston.constant.Constant.DB_USER;

public class UserRepositoryImpl implements UserRepository {
    private final CustomUserJdbcTemplate customUserJdbcTemplate = new CustomUserJdbcTemplate();

    @Override
    public User getUserById(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = customUserJdbcTemplate.query(sql, new UserRowMapper(), List.of(id));
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return customUserJdbcTemplate.query(sql, new UserRowMapper(), Collections.emptyList());
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        List<Object> params = List.of(user.getName(), user.getEmail());
        return customUserJdbcTemplate.insert(sql, new UserRowMapper(), params);
    }

    @Override
    public User updateUser(User user) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        List<Object> params = new ArrayList<>();

        if (user.getName() != null) {
            sql.append("name = ?, ");
            params.add(user.getName());
        }

        if (user.getEmail() != null) {
            sql.append("email = ?, ");
            params.add(user.getEmail());
        }

        if (params.isEmpty()) {
            throw new IllegalArgumentException("No fields to update");
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE user_id = ?");
        params.add(user.getId());

        int affectedRows = customUserJdbcTemplate.update(sql.toString(), params);
        if (affectedRows > 0) {
            return getUserById(user.getId());
        }
        return null;
    }

    @Override
    public int deleteUser(Long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        List<Object> params = Collections.singletonList(id);
        return customUserJdbcTemplate.delete(sql, params);
    }

    @Override
    public List<User> findUsersByIds(Set<Long> userIds) {
        List<User> users = new ArrayList<>();
        if (userIds.isEmpty()) {
            return users;
        }

        String inClause = userIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String query = "SELECT * FROM users WHERE user_id IN (" + inClause + ")";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("user_id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
