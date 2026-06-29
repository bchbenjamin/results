package com.hackathon.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Thread-safe utility to retrieve JDBC database connections.
 * Reads configurations dynamically from classpath:db.properties.
 */
public class DatabaseConnection {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find db.properties in resources");
            }
            properties.load(input);
            // Load H2 driver explicitly to ensure it registers (optional but safe)
            Class.forName("org.h2.Driver");
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Failed to initialize database connection configuration: " + e.getMessage());
        }
    }

    /**
     * Obtains a new database connection.
     * Caller is responsible for closing the connection, preferably using try-with-resources.
     *
     * @return a valid {@link Connection} object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.username"),
            properties.getProperty("db.password")
        );
    }
}
