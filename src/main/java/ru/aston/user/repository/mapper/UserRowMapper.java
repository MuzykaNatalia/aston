package ru.aston.user.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.aston.user.model.User;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .build();
    }
}
