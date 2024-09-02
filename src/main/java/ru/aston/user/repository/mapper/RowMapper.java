package ru.aston.user.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.aston.user.model.User;

public interface RowMapper<T> {
    User mapRow(ResultSet rs) throws SQLException;
}
