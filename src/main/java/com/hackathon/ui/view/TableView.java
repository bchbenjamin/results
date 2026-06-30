package com.hackathon.ui.view;

import com.hackathon.dao.StudentResultDAO;
import com.hackathon.model.Student;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

/**
 * Table view displaying all student results with search functionality.
 */
public class TableView extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton clearButton;
    
    private final StudentResultDAO dao;

    public TableView() {
        this.dao = new StudentResultDAO();
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Search Bar
        JPanel topPanel = new JPanel(new MigLayout("", "[grow][][][]", "[]"));
        searchField = new JTextField(20);
        searchButton = new JButton("Search by USN");
        clearButton = new JButton("Clear Search");

        topPanel.add(new JLabel("USN:"));
        topPanel.add(searchField, "growx");
        topPanel.add(searchButton);
        topPanel.add(clearButton);
        
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table
        String[] columns = {"USN", "Name", "Email", "Sub 1", "Sub 2", "Sub 3", "Total", "Average", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Listeners
        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });
    }

    public void refreshTable() {
        tableModel.setRowCount(0); // clear
        List<Student> students = dao.findAll();
        for (Student s : students) {
            addStudentToTable(s);
        }
    }

    private void performSearch() {
        String usn = searchField.getText().trim();
        if (usn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a USN to search.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<Student> studentOpt = dao.findById(usn);
        tableModel.setRowCount(0);
        if (studentOpt.isPresent()) {
            addStudentToTable(studentOpt.get());
        } else {
            JOptionPane.showMessageDialog(this, "No student found with USN: " + usn, "Search Result", JOptionPane.INFORMATION_MESSAGE);
            refreshTable(); // reset table if not found
        }
    }

    private void addStudentToTable(Student s) {
        tableModel.addRow(new Object[]{
                s.getUsn(),
                s.getName(),
                s.getEmail(),
                String.format("%.2f", s.getSub1()),
                String.format("%.2f", s.getSub2()),
                String.format("%.2f", s.getSub3()),
                String.format("%.2f", s.getTotal()),
                String.format("%.2f", s.getAverage()),
                s.getGrade()
        });
    }
}
