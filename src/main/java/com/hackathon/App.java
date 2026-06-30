package com.hackathon;

import com.hackathon.db.DatabaseInitializer;
import com.hackathon.ui.MainWindow;

import javax.swing.*;

/**
 * Entry point for the Swing and JDBC Boilerplate application.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Starting application boot sequence...");

        // 1. Initialize Database Schema DDL and Seed Data
        try {
            DatabaseInitializer.initializeDatabase();
            com.hackathon.db.DataSeeder.seedAllData();
        } catch (Exception e) {
            System.err.println("Fatal error: Database initialization or seeding failed. Exiting application.");
            System.exit(1);
        }

        // 2. Launch Graphical Interface on AWT Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainWindow window = new MainWindow();
                window.setVisible(true);
                System.out.println("Application GUI displayed successfully.");
            } catch (Exception e) {
                System.err.println("Fatal error launching GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
