package ru.aston.constant;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String SCHEMA_SQL_FILE = "schema.sql";
    public static final String HEADER_USER = "X-User-Id";
}
