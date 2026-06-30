package com.hackathon.ui.view;

import com.hackathon.dao.StudentResultDAO;
import com.hackathon.model.Student;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

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

        JPanel topPanel = new JPanel(new MigLayout("", "[grow][][][]", "[]"));
        searchField = new JTextField(20);
        searchButton = new JButton("Search by USN");
        clearButton = new JButton("Clear Search");

        topPanel.add(new JLabel("USN:"));
        topPanel.add(searchField, "growx");
        topPanel.add(searchButton);
        topPanel.add(clearButton);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"USN", "Name", "Batch", "Team", "Topic", "DSA", "ADA", "DBMS", "Math", "Python", "Java", "SIP", "Total", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
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
            refreshTable();
        }
    }

    private void addStudentToTable(Student s) {
        tableModel.addRow(new Object[]{
                s.getUsn(),
                s.getName(),
                s.getBatch(),
                s.getTeamNo(),
                s.getTopic(),
                String.format("%.2f", s.getDsa()),
                String.format("%.2f", s.getAda()),
                String.format("%.2f", s.getDbms()),
                String.format("%.2f", s.getMath()),
                String.format("%.2f", s.getPython()),
                String.format("%.2f", s.getJavaMarks()),
                String.format("%.2f", s.getSip()),
                String.format("%.2f", s.getTotalScore()),
                s.calculateGrade()
        });
    }
}
