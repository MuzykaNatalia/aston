package ru.aston.constant;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final String DB_PROPERTIES_FILE = "application.properties";
    public static final String SCHEMA_SQL_FILE = "schema.sql";
    public static final String DRIVER_NAME = "org.postgresql.Driver";
    public static final String HEADER_USER = "X-User-Id";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/shareit_db";
    public static final String DB_USER = "shareit";
    public static final String DB_PASSWORD = "admin";
}
