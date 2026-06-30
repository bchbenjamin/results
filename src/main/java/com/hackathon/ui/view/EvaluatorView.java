package com.hackathon.ui.view;

import com.hackathon.dao.EvaluatorDAO;
import com.hackathon.dao.StudentResultDAO;
import com.hackathon.model.Evaluator;
import com.hackathon.model.Student;
import com.hackathon.ui.NavigationController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.Optional;

public class EvaluatorView extends JPanel {
    private final NavigationController nav;
    private final EvaluatorDAO evalDao;
    private final StudentResultDAO studentDao;
    private Evaluator currentEvaluator;

    private JLabel welcomeLabel;
    private JTable table;
    private DefaultTableModel tableModel;

    public EvaluatorView(NavigationController nav) {
        this.nav = nav;
        this.evalDao = new EvaluatorDAO();
        this.studentDao = new StudentResultDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new MigLayout("", "[grow][]", "[]"));
        welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16f));
        topPanel.add(welcomeLabel, "growx");

        JButton backBtn = new JButton("Logout");
        backBtn.addActionListener(e -> nav.showView("RoleSelection"));
        topPanel.add(backBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"USN", "Name", "Topic", "Marks (Your Subject)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // only marks are editable
            }
        };
        
        tableModel.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (col == 3) {
                    updateStudentMarks(row);
                }
            }
        });

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> refreshTable());
        add(refreshBtn, BorderLayout.SOUTH);
    }

    public boolean loadEvaluator(String evaluatorId) {
        Optional<Evaluator> opt = evalDao.findById(evaluatorId);
        if (opt.isPresent()) {
            currentEvaluator = opt.get();
            welcomeLabel.setText("Evaluator: " + currentEvaluator.getName() + " | Subject: " + currentEvaluator.getCoreSubject());
            refreshTable();
            return true;
        }
        return false;
    }

    private void refreshTable() {
        if (currentEvaluator == null) return;
        tableModel.setRowCount(0);
        List<Student> students = studentDao.findAll();
        for (Student s : students) {
            double relevantMarks = getRelevantMarks(s);
            // If the core subject is SIP, only show students for whom this evaluator is the mentor
            if ("SIP".equalsIgnoreCase(currentEvaluator.getCoreSubject())) {
                if (evalDao.isSipMentorForTopic(currentEvaluator.getEvaluatorId(), s.getTopic())) {
                    addStudentToTable(s, relevantMarks);
                }
            } else {
                addStudentToTable(s, relevantMarks);
            }
        }
    }

    private void addStudentToTable(Student s, double marks) {
        tableModel.addRow(new Object[]{
                s.getUsn(),
                s.getName(),
                s.getTopic(),
                marks
        });
    }

    private double getRelevantMarks(Student s) {
        String subj = currentEvaluator.getCoreSubject().toLowerCase();
        switch (subj) {
            case "dsa": return s.getDsa();
            case "ada": return s.getAda();
            case "dbms": return s.getDbms();
            case "math": return s.getMath();
            case "python": return s.getPython();
            case "java": return s.getJavaMarks();
            case "sip": return s.getSip();
            default: return 0.0;
        }
    }

    private void updateStudentMarks(int row) {
        String usn = (String) tableModel.getValueAt(row, 0);
        String valStr = tableModel.getValueAt(row, 3).toString();
        try {
            double newMarks = Double.parseDouble(valStr);
            if (newMarks < 0 || newMarks > 100) throw new NumberFormatException();
            
            Optional<Student> sOpt = studentDao.findById(usn);
            if (sOpt.isPresent()) {
                Student s = sOpt.get();
                String subj = currentEvaluator.getCoreSubject().toLowerCase();
                switch (subj) {
                    case "dsa": s.setDsa(newMarks); break;
                    case "ada": s.setAda(newMarks); break;
                    case "dbms": s.setDbms(newMarks); break;
                    case "math": s.setMath(newMarks); break;
                    case "python": s.setPython(newMarks); break;
                    case "java": s.setJavaMarks(newMarks); break;
                    case "sip": s.setSip(newMarks); break;
                }
                studentDao.save(s);
                JOptionPane.showMessageDialog(this, "Marks updated successfully.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid marks entered.", "Error", JOptionPane.ERROR_MESSAGE);
            refreshTable(); // revert invalid change
        }
    }
}
