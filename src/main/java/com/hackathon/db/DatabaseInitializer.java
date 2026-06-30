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
        String createStudents = "CREATE TABLE IF NOT EXISTS Students ("
                + "USN VARCHAR(20) PRIMARY KEY, "
                + "Name VARCHAR(255) NOT NULL, "
                + "Batch INT NOT NULL, "
                + "Team_No INT NOT NULL, "
                + "Topic VARCHAR(255) NOT NULL"
                + ");";

        String createMarks = "CREATE TABLE IF NOT EXISTS Marks ("
                + "USN VARCHAR(20) PRIMARY KEY, "
                + "DSA DOUBLE, "
                + "ADA DOUBLE, "
                + "DBMS DOUBLE, "
                + "Math DOUBLE, "
                + "Python DOUBLE, "
                + "Java DOUBLE, "
                + "SIP DOUBLE, "
                + "Total_Score DOUBLE, "
                + "FOREIGN KEY (USN) REFERENCES Students(USN) ON DELETE CASCADE"
                + ");";

        String createEvaluators = "CREATE TABLE IF NOT EXISTS Evaluators ("
                + "Evaluator_ID VARCHAR(20) PRIMARY KEY, "
                + "Name VARCHAR(255) NOT NULL, "
                + "Core_Subject VARCHAR(50) NOT NULL"
                + ");";

        String createSIPMapping = "CREATE TABLE IF NOT EXISTS SIP_Mapping ("
                + "Topic VARCHAR(255) PRIMARY KEY, "
                + "Evaluator_ID VARCHAR(20), "
                + "FOREIGN KEY (Evaluator_ID) REFERENCES Evaluators(Evaluator_ID) ON DELETE CASCADE"
                + ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createStudents);
            stmt.execute(createMarks);
            stmt.execute(createEvaluators);
            stmt.execute(createSIPMapping);
            System.out.println("Database schema initialized: Students, Marks, Evaluators, SIP_Mapping created.");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}
