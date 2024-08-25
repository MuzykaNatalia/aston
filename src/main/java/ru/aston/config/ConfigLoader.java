package ru.aston.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConfigLoader {
    private static final String DB_PROPERTIES_FILE = "application.properties";
    private static final String SCHEMA_SQL_FILE = "schema.sql";
    private static Properties props;

    static {
        loadProperties();
        createDatabaseSchema();
    }

    public static void loadProperties() {
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE)) {
            if (is == null) {
                throw new RuntimeException("Could not find " + DB_PROPERTIES_FILE);
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties", e);
        }
    }

    public static void createDatabaseSchema() {
        String url = getDbUrl();
        String user = getDbUser();
        String password = getDbPassword();

        try {
            Class.forName("org.postgresql.Driver");

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 Statement statement = connection.createStatement()) {

                InputStream sqlStream = ConfigLoader.class.getClassLoader().getResourceAsStream(SCHEMA_SQL_FILE);
                if (sqlStream == null) {
                    throw new RuntimeException("Could not find schema.sql");
                }

                String sql = new String(sqlStream.readAllBytes());
                statement.execute(sql);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database schema. " +
                    "Please check your database connection parameters.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading SQL script from file.", e);
        }
    }

    public static String getDbUrl() {
        return props.getProperty("db.url");
    }

    public static String getDbUser() {
        return props.getProperty("db.user");
    }

    public static String getDbPassword() {
        return props.getProperty("db.password");
    }
}
