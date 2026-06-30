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
 * Adapted for the simple 3-subject student result schema.
 */
public class DataSeeder {

    public static void seedAllData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Starting data seeding process for Student Results...");
            seedStudents(conn, "dataset/cleaned_data.csv");
            System.out.println("Data seeding completed successfully.");
        } catch (SQLException e) {
            System.err.println("Error during data seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedStudents(Connection conn, String filePath) {
        String sql = "MERGE INTO student_results (usn, name, email, sub1, sub2, sub3, total, average, grade) KEY(usn) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // Skip header
                
                String[] values = line.split(",");
                // Assuming CSV is: Team_No,Batch,Name,USN,Topic,DSA,ADA,DBMS,Math,Python,Java,SIP,Total
                // We will map: Name->values[2], USN->values[3], Sub1(DSA)->values[5], Sub2(ADA)->values[6], Sub3(DBMS)->values[7]
                if (values.length >= 8) {
                    try {
                        String name = values[2].trim();
                        String usn = values[3].trim();
                        // Generate a mock email since it's not in the dataset
                        String email = usn.toLowerCase() + "@student.edu";
                        
                        double sub1 = Double.parseDouble(values[5].trim());
                        double sub2 = Double.parseDouble(values[6].trim());
                        double sub3 = Double.parseDouble(values[7].trim());
                        
                        // Use the OOP model to calculate derived fields!
                        Student temp = new Student(usn, name, email, sub1, sub2, sub3);
                        
                        ps.setString(1, temp.getUsn());
                        ps.setString(2, temp.getName());
                        ps.setString(3, temp.getEmail());
                        ps.setDouble(4, temp.getSub1());
                        ps.setDouble(5, temp.getSub2());
                        ps.setDouble(6, temp.getSub3());
                        ps.setDouble(7, temp.getTotal());
                        ps.setDouble(8, temp.getAverage());
                        ps.setString(9, temp.getGrade());
                        
                        ps.addBatch();
                    } catch (NumberFormatException ignored) {
                        // Skip malformed rows
                    }
                }
            }
            ps.executeBatch();
            System.out.println("Student results seeded.");
        } catch (IOException | SQLException e) {
            System.err.println("Failed to seed students: " + e.getMessage());
        }
    }
}
