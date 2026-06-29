package com.hackathon.db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Initializes the database schema automatically on application startup.
 */
public class DatabaseInitializer {

    /**
     * Executes DDL to set up the necessary database tables.
     */
    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS items ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "description VARCHAR(1000), "
                + "price DECIMAL(10, 2) NOT NULL, "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSQL);
            System.out.println("Database schema checked/initialized successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}
