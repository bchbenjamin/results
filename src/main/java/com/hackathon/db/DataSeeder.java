package com.hackathon.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.hackathon.model.Student;

/**
 * Utility to seed the database with initial CSV data.
 */
public class DataSeeder {

    public static void seedAllData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Starting data seeding process...");
            seedEvaluators(conn, "dataset/evaluators.csv");
            seedSIPMapping(conn, "dataset/sip_mentor_allocations.csv");
            seedStudentsAndMarks(conn, "dataset/cleaned_data.csv");
            System.out.println("Data seeding completed successfully.");
        } catch (SQLException e) {
            System.err.println("Error during data seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedEvaluators(Connection conn, String filePath) {
        String sql = "INSERT INTO Evaluators (Evaluator_ID, Name, Core_Subject) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE Name=VALUES(Name), Core_Subject=VALUES(Core_Subject)";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] values = line.split(",");
                if (values.length >= 3) {
                    ps.setString(1, values[0].trim());
                    ps.setString(2, values[1].trim());
                    ps.setString(3, values[2].trim());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            System.out.println("Evaluators seeded.");
        } catch (IOException | SQLException e) {
            System.err.println("Failed to seed evaluators: " + e.getMessage());
        }
    }

    private static void seedSIPMapping(Connection conn, String filePath) {
        String sql = "INSERT INTO SIP_Mapping (Topic, Evaluator_ID) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE Evaluator_ID=VALUES(Evaluator_ID)";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] values = line.split(",");
                if (values.length >= 2) {
                    ps.setString(1, values[0].trim());
                    ps.setString(2, values[1].trim());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            System.out.println("SIP mapping seeded.");
        } catch (IOException | SQLException e) {
            System.err.println("Failed to seed SIP mapping: " + e.getMessage());
        }
    }

    private static void seedStudentsAndMarks(Connection conn, String filePath) {
        String sqlStudent = "INSERT INTO Students (USN, Name, Batch, Team_No, Topic) VALUES (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE Name=VALUES(Name), Batch=VALUES(Batch), Team_No=VALUES(Team_No), Topic=VALUES(Topic)";
        String sqlMarks = "INSERT INTO Marks (USN, DSA, ADA, DBMS, Math, Python, Java, SIP, Total_Score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                          "ON DUPLICATE KEY UPDATE DSA=VALUES(DSA), ADA=VALUES(ADA), DBMS=VALUES(DBMS), Math=VALUES(Math), Python=VALUES(Python), Java=VALUES(Java), SIP=VALUES(SIP), Total_Score=VALUES(Total_Score)";
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement psStudent = conn.prepareStatement(sqlStudent);
             PreparedStatement psMarks = conn.prepareStatement(sqlMarks)) {
            
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } 
                
                String[] values = line.split(",");
                // Team_No(0),Batch(1),Name(2),USN(3),Topic(4),DSA(5),ADA(6),DBMS(7),Math(8),Python(9),Java(10),SIP(11),Total(12)
                if (values.length >= 12) {
                    try {
                        int teamNo = Integer.parseInt(values[0].trim());
                        int batch = Integer.parseInt(values[1].trim());
                        String name = values[2].trim();
                        String usn = values[3].trim();
                        String topic = values[4].trim();
                        
                        double dsa = Double.parseDouble(values[5].trim());
                        double ada = Double.parseDouble(values[6].trim());
                        double dbms = Double.parseDouble(values[7].trim());
                        double math = Double.parseDouble(values[8].trim());
                        double python = Double.parseDouble(values[9].trim());
                        double javaMarks = Double.parseDouble(values[10].trim());
                        double sip = Double.parseDouble(values[11].trim());
                        
                        Student temp = new Student(usn, name, batch, teamNo, topic, dsa, ada, dbms, math, python, javaMarks, sip);
                        
                        psStudent.setString(1, temp.getUsn());
                        psStudent.setString(2, temp.getName());
                        psStudent.setInt(3, temp.getBatch());
                        psStudent.setInt(4, temp.getTeamNo());
                        psStudent.setString(5, temp.getTopic());
                        psStudent.addBatch();
                        
                        psMarks.setString(1, temp.getUsn());
                        psMarks.setDouble(2, temp.getDsa());
                        psMarks.setDouble(3, temp.getAda());
                        psMarks.setDouble(4, temp.getDbms());
                        psMarks.setDouble(5, temp.getMath());
                        psMarks.setDouble(6, temp.getPython());
                        psMarks.setDouble(7, temp.getJavaMarks());
                        psMarks.setDouble(8, temp.getSip());
                        psMarks.setDouble(9, temp.getTotalScore());
                        psMarks.addBatch();
                    } catch (NumberFormatException ignored) {}
                }
            }
            psStudent.executeBatch();
            psMarks.executeBatch();
            System.out.println("Students and Marks seeded.");
        } catch (IOException | SQLException e) {
            System.err.println("Failed to seed students and marks: " + e.getMessage());
        }
    }
}
