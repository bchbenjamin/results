package com.hackathon.ui.view;

import com.hackathon.dao.StudentResultDAO;
import com.hackathon.model.Student;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class FormView extends JPanel {

    private JTextField usnField, nameField, batchField, teamNoField, topicField;
    private JTextField dsaField, adaField, dbmsField, mathField, pythonField, javaField, sipField;
    
    private JLabel totalLabel, gradeLabel;

    private JButton calculateButton, saveButton, clearButton;

    private final StudentResultDAO dao;
    private final TableView tableViewToRefresh;

    public FormView(TableView tableViewToRefresh) {
        this.dao = new StudentResultDAO();
        this.tableViewToRefresh = tableViewToRefresh;
        initComponents();
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap 4, insets 20", "[right][grow,fill][right][grow,fill]", "[]10[]"));

        add(new JLabel("USN:")); usnField = new JTextField(20); add(usnField);
        add(new JLabel("Name:")); nameField = new JTextField(20); add(nameField);
        
        add(new JLabel("Batch:")); batchField = new JTextField(20); add(batchField);
        add(new JLabel("Team No:")); teamNoField = new JTextField(20); add(teamNoField);
        
        add(new JLabel("Topic:")); topicField = new JTextField(20); add(topicField, "span 3");

        add(new JSeparator(), "span, growx, wrap");

        add(new JLabel("DSA Marks:")); dsaField = new JTextField(10); add(dsaField);
        add(new JLabel("ADA Marks:")); adaField = new JTextField(10); add(adaField);
        
        add(new JLabel("DBMS Marks:")); dbmsField = new JTextField(10); add(dbmsField);
        add(new JLabel("Math Marks:")); mathField = new JTextField(10); add(mathField);
        
        add(new JLabel("Python Marks:")); pythonField = new JTextField(10); add(pythonField);
        add(new JLabel("Java Marks:")); javaField = new JTextField(10); add(javaField);
        
        add(new JLabel("SIP Marks:")); sipField = new JTextField(10); add(sipField);
        add(new JLabel(""), "span 2"); // placeholder

        add(new JSeparator(), "span, growx, wrap");
        
        add(new JLabel("Calculated Total:"));
        totalLabel = new JLabel("0.00");
        totalLabel.setFont(totalLabel.getFont().deriveFont(java.awt.Font.BOLD));
        add(totalLabel, "span 3");

        add(new JLabel("Calculated Grade:"));
        gradeLabel = new JLabel("N/A");
        gradeLabel.setFont(gradeLabel.getFont().deriveFont(java.awt.Font.BOLD));
        add(gradeLabel, "span 3");

        add(new JSeparator(), "span, growx, wrap");

        JPanel buttonPanel = new JPanel(new MigLayout("", "[grow][grow][grow]", ""));
        calculateButton = new JButton("Calculate");
        saveButton = new JButton("Save Result");
        clearButton = new JButton("Clear Form");

        buttonPanel.add(calculateButton, "growx");
        buttonPanel.add(saveButton, "growx");
        buttonPanel.add(clearButton, "growx");

        add(buttonPanel, "span 4, growx");

        calculateButton.addActionListener(e -> calculateLocally());
        saveButton.addActionListener(e -> saveResult());
        clearButton.addActionListener(e -> clearForm());
    }

    private Student calculateLocally() {
        try {
            String usn = usnField.getText().trim();
            String name = nameField.getText().trim();
            String topic = topicField.getText().trim();

            if (usn.isEmpty() || name.isEmpty() || topic.isEmpty()) {
                JOptionPane.showMessageDialog(this, "USN, Name, and Topic are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            int batch = Integer.parseInt(batchField.getText().trim());
            int teamNo = Integer.parseInt(teamNoField.getText().trim());

            double dsa = Double.parseDouble(dsaField.getText().trim());
            double ada = Double.parseDouble(adaField.getText().trim());
            double dbms = Double.parseDouble(dbmsField.getText().trim());
            double math = Double.parseDouble(mathField.getText().trim());
            double python = Double.parseDouble(pythonField.getText().trim());
            double javaMarks = Double.parseDouble(javaField.getText().trim());
            double sip = Double.parseDouble(sipField.getText().trim());

            Student student = new Student(usn, name, batch, teamNo, topic, dsa, ada, dbms, math, python, javaMarks, sip);
            
            totalLabel.setText(String.format("%.2f", student.getTotalScore()));
            gradeLabel.setText(student.calculateGrade());

            return student;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Validation Error", JOptionPane.ERROR_MESSAGE);
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
        usnField.setText(""); nameField.setText("");
        batchField.setText(""); teamNoField.setText(""); topicField.setText("");
        dsaField.setText(""); adaField.setText(""); dbmsField.setText("");
        mathField.setText(""); pythonField.setText(""); javaField.setText(""); sipField.setText("");
        totalLabel.setText("0.00"); gradeLabel.setText("N/A");
    }
}
