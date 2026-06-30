package com.hackathon.ui.view;

import com.hackathon.dao.StudentResultDAO;
import com.hackathon.model.Student;
import com.hackathon.ui.NavigationController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Font;
import java.util.Optional;

public class StudentView extends JPanel {
    private final StudentResultDAO dao;
    private final NavigationController nav;
    private Student currentStudent;
    
    private JLabel nameLabel, usnLabel, topicLabel;
    private JLabel dsaLabel, adaLabel, dbmsLabel, mathLabel, pythonLabel, javaLabel, sipLabel;
    private JLabel totalLabel, gradeLabel;

    public StudentView(NavigationController nav) {
        this.nav = nav;
        this.dao = new StudentResultDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap 2, insets 20", "[right][grow,fill]", "[]15[]"));

        JButton backBtn = new JButton("Back to Roles");
        backBtn.addActionListener(e -> nav.showView("RoleSelection"));
        add(backBtn, "span 2, left, wrap");

        add(new JLabel("Name:"));
        nameLabel = new JLabel(); nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD)); add(nameLabel);
        
        add(new JLabel("USN:"));
        usnLabel = new JLabel(); usnLabel.setFont(usnLabel.getFont().deriveFont(Font.BOLD)); add(usnLabel);
        
        add(new JLabel("Topic:"));
        topicLabel = new JLabel(); topicLabel.setFont(topicLabel.getFont().deriveFont(Font.BOLD)); add(topicLabel);

        add(new JSeparator(), "span 2, growx, wrap");

        add(new JLabel("DSA:")); dsaLabel = new JLabel(); add(dsaLabel);
        add(new JLabel("ADA:")); adaLabel = new JLabel(); add(adaLabel);
        add(new JLabel("DBMS:")); dbmsLabel = new JLabel(); add(dbmsLabel);
        add(new JLabel("Math:")); mathLabel = new JLabel(); add(mathLabel);
        add(new JLabel("Python:")); pythonLabel = new JLabel(); add(pythonLabel);
        add(new JLabel("Java:")); javaLabel = new JLabel(); add(javaLabel);
        add(new JLabel("SIP:")); sipLabel = new JLabel(); add(sipLabel);

        add(new JSeparator(), "span 2, growx, wrap");

        add(new JLabel("Total Score:"));
        totalLabel = new JLabel(); totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 18f)); add(totalLabel);
        
        add(new JLabel("Grade:"));
        gradeLabel = new JLabel(); gradeLabel.setFont(gradeLabel.getFont().deriveFont(Font.BOLD, 18f)); add(gradeLabel);
    }

    public boolean loadStudent(String usn) {
        Optional<Student> opt = dao.findById(usn);
        if (opt.isPresent()) {
            currentStudent = opt.get();
            nameLabel.setText(currentStudent.getName());
            usnLabel.setText(currentStudent.getUsn());
            topicLabel.setText(currentStudent.getTopic());
            
            dsaLabel.setText(String.format("%.2f", currentStudent.getDsa()));
            adaLabel.setText(String.format("%.2f", currentStudent.getAda()));
            dbmsLabel.setText(String.format("%.2f", currentStudent.getDbms()));
            mathLabel.setText(String.format("%.2f", currentStudent.getMath()));
            pythonLabel.setText(String.format("%.2f", currentStudent.getPython()));
            javaLabel.setText(String.format("%.2f", currentStudent.getJavaMarks()));
            sipLabel.setText(String.format("%.2f", currentStudent.getSip()));
            
            totalLabel.setText(String.format("%.2f", currentStudent.getTotalScore()));
            gradeLabel.setText(currentStudent.calculateGrade());
            return true;
        }
        return false;
    }
}
