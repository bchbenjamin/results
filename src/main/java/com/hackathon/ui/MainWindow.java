package com.hackathon.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.hackathon.ui.view.FormView;
import com.hackathon.ui.view.TableView;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the Student Result Management System.
 */
public class MainWindow extends JFrame {

    private JTabbedPane tabbedPane;

    public MainWindow() {
        // Set up the modern look and feel
        setupLookAndFeel();

        setTitle("Student Result Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        TableView tableView = new TableView();
        FormView formView = new FormView(tableView); // pass table view so form can refresh it

        tabbedPane.addTab("View Results", tableView);
        tabbedPane.addTab("Add / Edit Result", formView);

        add(tabbedPane, BorderLayout.CENTER);
    }
}
