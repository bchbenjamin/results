package com.hackathon.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Utility to seed the database with initial CSV data.
 */
public class DataSeeder {

    public static void seedAllData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Starting data seeding process...");
            seedEvaluators(conn, "dataset/evaluators.csv");
            seedStudentsAndMarks(conn, "dataset/cleaned_data.csv");
            seedSipMapping(conn, "dataset/sip_mentor_allocations.csv");
            System.out.println("Data seeding completed successfully.");
        } catch (SQLException e) {
            System.err.println("Error during data seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedEvaluators(Connection conn, String filePath) {
        String sql = "MERGE INTO evaluators (evaluator_id, name, core_subject) KEY(evaluator_id) VALUES (?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // Skip header
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

    private static void seedStudentsAndMarks(Connection conn, String filePath) {
        String studentSql = "MERGE INTO students (usn, name, batch, team_no, topic) KEY(usn) VALUES (?, ?, ?, ?, ?)";
        String marksSql = "MERGE INTO marks (usn, dsa, ada, dbms, math, python, java, sip, total_score) KEY(usn) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement psStudent = conn.prepareStatement(studentSql);
             PreparedStatement psMarks = conn.prepareStatement(marksSql)) {
            
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // Skip header
                // Team_No,Batch,Name,USN,Topic,DSA,ADA,DBMS,Math,Python,Java,SIP,Total
                // 0      ,1    ,2   ,3  ,4    ,5  ,6  ,7   ,8   ,9     ,10  ,11 ,12
                String[] values = line.split(",");
                if (values.length >= 13) {
                    // Extract fields
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
                    double java = Double.parseDouble(values[10].trim());
                    double sip = Double.parseDouble(values[11].trim());
                    double total = Double.parseDouble(values[12].trim());

                    // Add Student batch
                    psStudent.setString(1, usn);
                    psStudent.setString(2, name);
                    psStudent.setInt(3, batch);
                    psStudent.setInt(4, teamNo);
                    psStudent.setString(5, topic);
                    psStudent.addBatch();

                    // Add Marks batch
                    psMarks.setString(1, usn);
                    psMarks.setDouble(2, dsa);
                    psMarks.setDouble(3, ada);
                    psMarks.setDouble(4, dbms);
                    psMarks.setDouble(5, math);
                    psMarks.setDouble(6, python);
                    psMarks.setDouble(7, java);
                    psMarks.setDouble(8, sip);
                    psMarks.setDouble(9, total);
                    psMarks.addBatch();
                }
            }
            psStudent.executeBatch();
            psMarks.executeBatch();
            System.out.println("Students and Marks seeded.");
        } catch (IOException | SQLException | NumberFormatException e) {
            System.err.println("Failed to seed students and marks: " + e.getMessage());
        }
    }

    private static void seedSipMapping(Connection conn, String filePath) {
        String sql = "MERGE INTO sip_mapping (topic, evaluator_id) KEY(topic) VALUES (?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // Skip header
                // Topic,SIP_Mentor_ID
                // Note: topic might contain commas if not careful, but our dataset is clean without quotes.
                // Safest to split on the last comma if needed, but our topics have no commas in sip_mentor_allocations.csv.
                int lastComma = line.lastIndexOf(',');
                if (lastComma > 0) {
                    String topic = line.substring(0, lastComma).trim();
                    String mentorId = line.substring(lastComma + 1).trim();
                    
                    ps.setString(1, topic);
                    ps.setString(2, mentorId);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            System.out.println("SIP Mapping seeded.");
        } catch (IOException | SQLException e) {
            System.err.println("Failed to seed SIP mapping: " + e.getMessage());
        }
    }
}
