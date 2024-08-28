package ru.aston.config;

public class DatabaseConfig {
    public static final String DB_DRIVER = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/shareit_db";
    public static final String DB_USER = "shareit";
    public static final String DB_PASSWORD = "admin";

    private DatabaseConfig() {
    }
}
