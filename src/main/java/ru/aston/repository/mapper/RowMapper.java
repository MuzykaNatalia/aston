package ru.aston.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.aston.model.User;

public interface RowMapper<T> {
    User mapRow(ResultSet rs) throws SQLException;
}
