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
        String createStudentsSQL = "CREATE TABLE IF NOT EXISTS students ("
                + "usn VARCHAR(20) PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "batch INT NOT NULL, "
                + "team_no INT NOT NULL, "
                + "topic VARCHAR(500) NOT NULL"
                + ");";

        String createMarksSQL = "CREATE TABLE IF NOT EXISTS marks ("
                + "usn VARCHAR(20) PRIMARY KEY, "
                + "dsa DOUBLE DEFAULT 0, "
                + "ada DOUBLE DEFAULT 0, "
                + "dbms DOUBLE DEFAULT 0, "
                + "math DOUBLE DEFAULT 0, "
                + "python DOUBLE DEFAULT 0, "
                + "java DOUBLE DEFAULT 0, "
                + "sip DOUBLE DEFAULT 0, "
                + "total_score DOUBLE DEFAULT 0, "
                + "FOREIGN KEY (usn) REFERENCES students(usn)"
                + ");";

        String createEvaluatorsSQL = "CREATE TABLE IF NOT EXISTS evaluators ("
                + "evaluator_id VARCHAR(10) PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "core_subject VARCHAR(50) NOT NULL"
                + ");";

        String createSipMappingSQL = "CREATE TABLE IF NOT EXISTS sip_mapping ("
                + "topic VARCHAR(500) PRIMARY KEY, "
                + "evaluator_id VARCHAR(10) NOT NULL, "
                + "FOREIGN KEY (evaluator_id) REFERENCES evaluators(evaluator_id)"
                + ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createStudentsSQL);
            stmt.execute(createMarksSQL);
            stmt.execute(createEvaluatorsSQL);
            stmt.execute(createSipMappingSQL);
            System.out.println("Database schema checked/initialized successfully with 4 tables.");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}
