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

        // 1. Initialize Database Schema DDL
        try {
            DatabaseInitializer.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Fatal error: Database initialization failed. Exiting application.");
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
