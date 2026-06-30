package com.hackathon.ui.view;

import com.hackathon.model.LeaderboardService;
import com.hackathon.model.Student;
import com.hackathon.ui.NavigationController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardView extends JPanel {
    private final NavigationController nav;
    private final LeaderboardService leaderboardService;
    private final String[] subjects = {"Overall", "DSA", "ADA", "DBMS", "Math", "Python", "Java", "SIP"};

    public LeaderboardView(NavigationController nav) {
        this.nav = nav;
        this.leaderboardService = new LeaderboardService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backBtn = new JButton("Back to Student View");
        backBtn.addActionListener(e -> nav.showView("StudentView"));
        topPanel.add(backBtn);
        
        JLabel title = new JLabel("  Top 5 Leaderboards");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        topPanel.add(title);
        
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        for (String subject : subjects) {
            tabbedPane.addTab(subject, createLeaderboardPanel(subject));
        }

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createLeaderboardPanel(String subject) {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Rank", "USN", "Name", "Topic", "Score (Out of 10)", "Total Score"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Load data initially
        refreshTableData(model, subject);

        // Provide a refresh button for this specific tab
        JButton refreshBtn = new JButton("Refresh " + subject + " Leaderboard");
        refreshBtn.addActionListener(e -> refreshTableData(model, subject));
        panel.add(refreshBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshTableData(DefaultTableModel model, String subject) {
        model.setRowCount(0);
        List<Student> top5;
        if (subject.equalsIgnoreCase("Overall")) {
            top5 = leaderboardService.getOverallTop5();
        } else {
            top5 = leaderboardService.getSubjectTop5(subject);
        }

        int rank = 1;
        for (Student s : top5) {
            double primaryScore = subject.equalsIgnoreCase("Overall") ? s.getTotalScore() : getSubjectScore(s, subject);
            model.addRow(new Object[]{
                    rank++,
                    s.getUsn(),
                    s.getName(),
                    s.getTopic(),
                    String.format("%.2f", primaryScore),
                    String.format("%.2f", s.getTotalScore())
            });
        }
    }
    
    private double getSubjectScore(Student s, String subject) {
        switch (subject.toLowerCase()) {
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
}
