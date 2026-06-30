package com.hackathon.ui.view;

import com.hackathon.dao.StudentResultDAO;
import com.hackathon.model.Student;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Form view to input and edit student result data.
 */
public class FormView extends JPanel {

    private JTextField usnField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField sub1Field;
    private JTextField sub2Field;
    private JTextField sub3Field;
    
    private JLabel totalLabel;
    private JLabel averageLabel;
    private JLabel gradeLabel;

    private JButton calculateButton;
    private JButton saveButton;
    private JButton clearButton;

    private final StudentResultDAO dao;
    private final TableView tableViewToRefresh;

    public FormView(TableView tableViewToRefresh) {
        this.dao = new StudentResultDAO();
        this.tableViewToRefresh = tableViewToRefresh;
        initComponents();
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap 2, insets 20", "[right][grow,fill]", "[]15[]"));

        // Add form fields
        add(new JLabel("USN:"));
        usnField = new JTextField(20);
        add(usnField);

        add(new JLabel("Name:"));
        nameField = new JTextField(20);
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField(20);
        add(emailField);

        add(new JLabel("Subject 1 Marks:"));
        sub1Field = new JTextField(20);
        add(sub1Field);

        add(new JLabel("Subject 2 Marks:"));
        sub2Field = new JTextField(20);
        add(sub2Field);

        add(new JLabel("Subject 3 Marks:"));
        sub3Field = new JTextField(20);
        add(sub3Field);

        // Results display labels
        add(new JSeparator(), "span, growx, wrap");
        
        add(new JLabel("Calculated Total:"));
        totalLabel = new JLabel("0.00");
        totalLabel.setFont(totalLabel.getFont().deriveFont(java.awt.Font.BOLD));
        add(totalLabel);

        add(new JLabel("Calculated Average:"));
        averageLabel = new JLabel("0.00");
        averageLabel.setFont(averageLabel.getFont().deriveFont(java.awt.Font.BOLD));
        add(averageLabel);

        add(new JLabel("Calculated Grade:"));
        gradeLabel = new JLabel("N/A");
        gradeLabel.setFont(gradeLabel.getFont().deriveFont(java.awt.Font.BOLD));
        add(gradeLabel);

        add(new JSeparator(), "span, growx, wrap");

        // Buttons panel
        JPanel buttonPanel = new JPanel(new MigLayout("", "[grow][grow][grow]", ""));
        calculateButton = new JButton("Calculate");
        saveButton = new JButton("Save Result");
        clearButton = new JButton("Clear Form");

        buttonPanel.add(calculateButton, "growx");
        buttonPanel.add(saveButton, "growx");
        buttonPanel.add(clearButton, "growx");

        add(buttonPanel, "span 2, growx");

        // Event Listeners
        calculateButton.addActionListener(e -> calculateLocally());
        saveButton.addActionListener(e -> saveResult());
        clearButton.addActionListener(e -> clearForm());
    }

    /**
     * Validates input and calculates derived fields locally.
     * Fulfills input validation constraints.
     */
    private Student calculateLocally() {
        try {
            String usn = usnField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (usn.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "USN and Name are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            double sub1 = Double.parseDouble(sub1Field.getText().trim());
            double sub2 = Double.parseDouble(sub2Field.getText().trim());
            double sub3 = Double.parseDouble(sub3Field.getText().trim());

            if (sub1 < 0 || sub1 > 100 || sub2 < 0 || sub2 > 100 || sub3 < 0 || sub3 > 100) {
                 JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                 return null;
            }

            // Create model object (inherits Person, implements Gradable)
            Student student = new Student(usn, name, email, sub1, sub2, sub3);
            
            // Update UI
            totalLabel.setText(String.format("%.2f", student.getTotal()));
            averageLabel.setText(String.format("%.2f", student.getAverage()));
            gradeLabel.setText(student.getGrade());

            return student;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for marks.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void saveResult() {
        Student student = calculateLocally();
        if (student != null) {
            try {
                dao.save(student);
                JOptionPane.showMessageDialog(this, "Student result saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                if (tableViewToRefresh != null) {
                    tableViewToRefresh.refreshTable();
                }
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        usnField.setText("");
        nameField.setText("");
        emailField.setText("");
        sub1Field.setText("");
        sub2Field.setText("");
        sub3Field.setText("");
        totalLabel.setText("0.00");
        averageLabel.setText("0.00");
        gradeLabel.setText("N/A");
    }
}
