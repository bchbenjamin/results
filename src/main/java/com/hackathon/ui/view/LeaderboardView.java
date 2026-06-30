package com.hackathon.ui.view;

import com.hackathon.model.LeaderboardService;
import com.hackathon.model.Student;
import com.hackathon.ui.NavigationController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

public class LeaderboardView extends JPanel {
    private final NavigationController nav;
    private final LeaderboardService leaderboardService;
    private JTabbedPane tabbedPane;

    public LeaderboardView(NavigationController nav) {
        this.nav = nav;
        this.leaderboardService = new LeaderboardService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("Back to Student Profile");
        backBtn.addActionListener(e -> nav.showView("StudentView"));
        topPanel.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Top 5 Leaderboards (Powered by 1v1 Gauntlet)", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f).deriveFont(java.awt.Font.BOLD));
        topPanel.add(title, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void loadLeaderboards() {
        tabbedPane.removeAll();
        
        tabbedPane.addTab("Overall", createTablePanel(leaderboardService.getTop5Overall(), "Total_Score"));
        tabbedPane.addTab("DSA", createTablePanel(leaderboardService.getTop5ForSubject("dsa"), "DSA"));
        tabbedPane.addTab("ADA", createTablePanel(leaderboardService.getTop5ForSubject("ada"), "ADA"));
        tabbedPane.addTab("DBMS", createTablePanel(leaderboardService.getTop5ForSubject("dbms"), "DBMS"));
        tabbedPane.addTab("Math", createTablePanel(leaderboardService.getTop5ForSubject("math"), "Math"));
        tabbedPane.addTab("Python", createTablePanel(leaderboardService.getTop5ForSubject("python"), "Python"));
        tabbedPane.addTab("Java", createTablePanel(leaderboardService.getTop5ForSubject("java"), "Java"));
        tabbedPane.addTab("SIP", createTablePanel(leaderboardService.getTop5ForSubject("sip"), "SIP"));
    }

    private JPanel createTablePanel(List<Student> students, String targetSubject) {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Rank", "USN", "Name", "Score", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        int rank = 1;
        for (Student s : students) {
            double targetScore = "Total_Score".equals(targetSubject) ? s.getTotalScore() : leaderboardService.getSubjectScore(s, targetSubject.toLowerCase());
            model.addRow(new Object[]{
                rank++,
                s.getUsn(),
                s.getName(),
                String.format("%.2f", targetScore),
                String.format("%.2f", s.getTotalScore())
            });
        }
        
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(table.getFont().deriveFont(14f));
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
