package com.hackathon.ui.view;

import com.hackathon.ui.NavigationController;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.Font;

public class RoleSelectionView extends JPanel {
    public RoleSelectionView(NavigationController nav, JFrame parentFrame) {
        setLayout(new MigLayout("wrap 1, insets 50, center", "[center]", "[]40[]20[]20[]"));
        
        JLabel title = new JLabel("Academic Evaluation Portal");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        add(title);
        
        JButton studentBtn = new JButton("Student View");
        studentBtn.setFont(studentBtn.getFont().deriveFont(18f));
        studentBtn.addActionListener(e -> {
            String usn = JOptionPane.showInputDialog(parentFrame, "Enter your USN:", "Student Login", JOptionPane.QUESTION_MESSAGE);
            if (usn != null && !usn.trim().isEmpty()) {
                StudentView sv = (StudentView) nav.getView("StudentView");
                if (sv.loadStudent(usn.trim())) {
                    nav.showView("StudentView");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid USN or Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(studentBtn, "growx, h 60!, w 300!");
        
        JButton evalBtn = new JButton("Evaluator View");
        evalBtn.setFont(evalBtn.getFont().deriveFont(18f));
        evalBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(parentFrame, "Enter Evaluator ID:", "Evaluator Login", JOptionPane.QUESTION_MESSAGE);
            if (id != null && !id.trim().isEmpty()) {
                EvaluatorView ev = (EvaluatorView) nav.getView("EvaluatorView");
                if (ev.loadEvaluator(id.trim())) {
                    nav.showView("EvaluatorView");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid Evaluator ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(evalBtn, "growx, h 60!, w 300!");
        
        JButton adminBtn = new JButton("Admin Panel");
        adminBtn.setFont(adminBtn.getFont().deriveFont(18f));
        adminBtn.addActionListener(e -> nav.showView("AdminView"));
        add(adminBtn, "growx, h 60!, w 300!");
    }
}
