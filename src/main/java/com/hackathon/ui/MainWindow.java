package com.hackathon.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.hackathon.dao.ItemDAO;
import com.hackathon.ui.view.FormView;
import com.hackathon.ui.view.TableView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window frame.
 * Implements the base GUI setup, initializes look and feel, and builds the primary screen layout.
 */
public class MainWindow extends JFrame {
    private final ItemDAO itemDAO;
    private NavigationController navController;

    public MainWindow() {
        // Initialize Look and Feel (FlatLaf Dark Theme)
        setupLookAndFeel();

        this.itemDAO = new ItemDAO();

        setTitle("Hackathon Inventory Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 680);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null); // Center on screen

        // Build the layout structure
        initComponents();
    }

    private void setupLookAndFeel() {
        try {
            // Apply Modern FlatLaf Dark theme
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaf theme, falling back to default.");
        }
    }

    private void initComponents() {
        // Main panel using MigLayout with two columns:
        // - Col 0: Sidebar (200px fixed width, no margin)
        // - Col 1: Main Content Container (fills remaining space)
        JPanel mainContainer = new JPanel(new MigLayout("insets 0, gap 0", "[200!]0[grow, fill]", "[grow, fill]"));
        setContentPane(mainContainer);

        // 1. Sidebar Panel
        JPanel sidebar = new JPanel(new MigLayout("insets 20 15 20 15, wrap 1, gap 10", "[grow, fill]", "[]15[]10[]grow[]"));
        sidebar.setBackground(new Color(33, 37, 41)); // Sleek dark charcoal background for contrast

        // Sidebar Header
        JLabel logoLabel = new JLabel("INVENTORY");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(new Color(13, 110, 253)); // Accent blue logo
        sidebar.add(logoLabel, "align center, gapbottom 10");

        // Navigation Buttons (Styled as toolbar buttons for a sleek, flat dashboard feel)
        JButton viewCatalogBtn = createSidebarButton("View Catalog", "CATALOG_VIEW");
        JButton addItemBtn = createSidebarButton("Add New Item", "FORM_VIEW");

        sidebar.add(viewCatalogBtn);
        sidebar.add(addItemBtn);

        // Sidebar Footer Info
        JLabel footerLabel = new JLabel("v1.0.0 Boilerplate");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footerLabel.setForeground(Color.GRAY);
        sidebar.add(footerLabel, "align center");

        mainContainer.add(sidebar);

        // 2. Content Card Panel
        JPanel contentCardPanel = new JPanel(new CardLayout());
        mainContainer.add(contentCardPanel);

        // 3. Navigation Controller Setup
        navController = new NavigationController(contentCardPanel);

        // Register Views
        TableView tableView = new TableView(itemDAO, navController);
        FormView formView = new FormView(itemDAO, navController);

        navController.registerView("CATALOG_VIEW", tableView);
        navController.registerView("FORM_VIEW", formView);

        // Action routing for sidebar buttons
        viewCatalogBtn.addActionListener(e -> navController.showView("CATALOG_VIEW"));
        addItemBtn.addActionListener(e -> navController.showView("FORM_VIEW"));

        // Load Default View
        navController.showView("CATALOG_VIEW");
    }

    private JButton createSidebarButton(String text, String targetView) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        
        // Use FlatLaf styling properties for elegant flat buttons
        btn.putClientProperty("JButton.buttonType", "toolBarButton");
        btn.setMargin(new Insets(10, 15, 10, 15));
        
        return btn;
    }
}
