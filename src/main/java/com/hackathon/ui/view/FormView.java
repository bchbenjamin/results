package com.hackathon.ui.view;

import com.hackathon.dao.ItemDAO;
import com.hackathon.model.Item;
import com.hackathon.ui.NavigationController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Modular data entry Form View using MigLayout.
 * Incorporates validation, clear visual prompts, and direct interaction with the model layer.
 */
public class FormView extends JPanel implements NavigationController.ViewLifecycle {
    private final ItemDAO itemDAO;
    private final NavigationController navController;

    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextArea descArea;
    private final JLabel statusLabel;

    public FormView(ItemDAO itemDAO, NavigationController navController) {
        this.itemDAO = itemDAO;
        this.navController = navController;

        // MigLayout configuration: 
        // - wrap 2: create 2-column grid layout
        // - insets 30: 30px padding around boundaries
        // - gap 15 15: 15px gap horizontally and vertically
        // - Column constraints: [right, pref!] (col 0 is right-aligned, minimum fit) 
        //                     [grow, fill] (col 1 occupies all remaining horizontal space)
        setLayout(new MigLayout("insets 30, wrap 2, gap 15 15", "[right, pref!]15[grow, fill]", "[]"));

        // Title Section
        JLabel titleLabel = new JLabel("Create Catalog Item");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(titleLabel, "span 2, gapbottom 20");

        // Form Fields
        add(new JLabel("Item Name:"));
        nameField = new JTextField();
        nameField.putClientProperty("JTextField.placeholderText", "Enter product name");
        add(nameField);

        add(new JLabel("Price ($):"));
        priceField = new JTextField();
        priceField.putClientProperty("JTextField.placeholderText", "0.00");
        add(priceField);

        add(new JLabel("Description:"));
        descArea = new JTextArea();
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.putClientProperty("JTextField.placeholderText", "Describe the item"); // Supported in FlatLaf for textarea as well
        JScrollPane descScrollPane = new JScrollPane(descArea);
        add(descScrollPane, "height 120::200");

        // Action Panel (Status Label and Buttons)
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> resetForm());

        JButton submitBtn = new JButton("Save Item");
        // Apply secondary styling command if supported (FlatLaf accent color styling)
        submitBtn.putClientProperty("JButton.buttonType", "accent");
        submitBtn.addActionListener(e -> saveItem());

        JPanel actionPanel = new JPanel(new MigLayout("insets 0, gap 10", "[grow][pref!][pref!]"));
        actionPanel.add(statusLabel, "grow");
        actionPanel.add(clearBtn, "width 90!");
        actionPanel.add(submitBtn, "width 120!");

        add(actionPanel, "span 2, growx, gaptop 15");
    }

    /**
     * Resets all input elements in the form.
     */
    private void resetForm() {
        nameField.setText("");
        priceField.setText("");
        descArea.setText("");
        statusLabel.setText(" ");
        statusLabel.setForeground(null);
    }

    /**
     * Reads form fields, performs validations, and persists entity via DAO.
     */
    private void saveItem() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String description = descArea.getText().trim();

        // 1. Validation: Check empty inputs
        if (name.isEmpty()) {
            showError("Item Name is required!");
            nameField.requestFocus();
            return;
        }

        if (priceText.isEmpty()) {
            showError("Price is required!");
            priceField.requestFocus();
            return;
        }

        // 2. Validation: Numeric check
        BigDecimal price;
        try {
            price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                showError("Price cannot be negative!");
                priceField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            showError("Price must be a valid decimal number!");
            priceField.requestFocus();
            return;
        }

        // 3. Persist Model
        try {
            Item item = new Item(name, description, price);
            itemDAO.save(item);
            
            // Show Success Notification
            JOptionPane.showMessageDialog(this,
                    "Item '" + name + "' saved successfully with ID " + item.getId() + "!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            
            resetForm();
            
            // Navigate back to the Catalog Table View
            navController.showView("CATALOG_VIEW");
        } catch (Exception e) {
            showError("Database error occurred: " + e.getMessage());
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.RED);
    }

    @Override
    public void onViewShown() {
        // Reset when entering page fresh
        resetForm();
        nameField.requestFocus();
    }
}
