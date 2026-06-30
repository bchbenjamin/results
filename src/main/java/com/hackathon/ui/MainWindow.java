package com.hackathon.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.hackathon.ui.view.FormView;
import com.hackathon.ui.view.TableView;
import com.hackathon.ui.view.RoleSelectionView;
import com.hackathon.ui.view.StudentView;
import com.hackathon.ui.view.EvaluatorView;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the Student Result Management System.
 */
public class MainWindow extends JFrame {

    private JPanel container;
    private NavigationController nav;

    public MainWindow() {
        setupLookAndFeel();

        setTitle("Academic Evaluation Portal");
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
        container = new JPanel(new CardLayout());
        nav = new NavigationController(container);
        
        // Admin View Wrapper
        JTabbedPane adminTabbedPane = new JTabbedPane();
        TableView tableView = new TableView();
        FormView formView = new FormView(tableView);
        adminTabbedPane.addTab("View Results", tableView);
        adminTabbedPane.addTab("Add / Edit Result", formView);
        
        JPanel adminWrapper = new JPanel(new BorderLayout());
        JButton backFromAdminBtn = new JButton("Back to Roles");
        backFromAdminBtn.addActionListener(e -> nav.showView("RoleSelection"));
        
        JPanel adminTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adminTopPanel.add(backFromAdminBtn);
        adminWrapper.add(adminTopPanel, BorderLayout.NORTH);
        adminWrapper.add(adminTabbedPane, BorderLayout.CENTER);

        // Register views
        nav.registerView("RoleSelection", new RoleSelectionView(nav, this));
        nav.registerView("StudentView", new StudentView(nav));
        nav.registerView("EvaluatorView", new EvaluatorView(nav));
        nav.registerView("AdminView", adminWrapper);

        add(container, BorderLayout.CENTER);
        
        nav.showView("RoleSelection");
    }
}
