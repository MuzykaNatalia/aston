package ru.aston.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import ru.aston.model.User;
import ru.aston.repository.mapper.UserRowMapper;

public class UserRepositoryImpl implements UserRepository {
    private final CustomJdbcTemplate customJdbcTemplate = new CustomJdbcTemplate();

    @Override
    public User getUserById(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = customJdbcTemplate.query(sql, new UserRowMapper(), List.of(id));
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return customJdbcTemplate.query(sql, new UserRowMapper(), Collections.emptyList());
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        List<Object> params = List.of(user.getName(), user.getEmail());
        return customJdbcTemplate.insert(sql, new UserRowMapper(), params);
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

        int affectedRows = customJdbcTemplate.update(sql.toString(), params);
        if (affectedRows > 0) {
            return getUserById(user.getId());
        }
        return null;
    }

    @Override
    public int deleteUser(Long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        List<Object> params = Collections.singletonList(id);
        return customJdbcTemplate.delete(sql, params);
    }
}
