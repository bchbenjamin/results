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
        String createTableSQL = "CREATE TABLE IF NOT EXISTS student_results ("
                + "usn VARCHAR(20) PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255), "
                + "sub1 DOUBLE NOT NULL, "
                + "sub2 DOUBLE NOT NULL, "
                + "sub3 DOUBLE NOT NULL, "
                + "total DOUBLE NOT NULL, "
                + "average DOUBLE NOT NULL, "
                + "grade VARCHAR(5) NOT NULL"
                + ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSQL);
            System.out.println("Database schema initialized: student_results table created.");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}
