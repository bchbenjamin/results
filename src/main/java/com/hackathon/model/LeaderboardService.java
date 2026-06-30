package com.hackathon.model;

import com.hackathon.dao.StudentResultDAO;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardService {

    private final StudentResultDAO studentDao;

    public LeaderboardService() {
        this.studentDao = new StudentResultDAO();
    }

    public List<Student> getOverallTop5() {
        List<Student> allStudents = studentDao.findAll();
        
        return allStudents.stream()
                .sorted(Comparator.comparingDouble(Student::getTotalScore).reversed()
                        .thenComparing(Student::getName, String.CASE_INSENSITIVE_ORDER))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Student> getSubjectTop5(String subject) {
        List<Student> allStudents = studentDao.findAll();

        Comparator<Student> gauntletComparator = (s1, s2) -> {
            // 1. Primary Sort
            double score1 = getSubjectScore(s1, subject);
            double score2 = getSubjectScore(s2, subject);
            int primaryCompare = Double.compare(score2, score1);
            if (primaryCompare != 0) {
                return primaryCompare;
            }

            // 2. The 1v1 Gauntlet Tie-Breaker
            int p1 = 0;
            int p2 = 0;
            String[] allSubjects = {"dsa", "ada", "dbms", "math", "python", "java", "sip"};
            for (String sub : allSubjects) {
                if (!sub.equalsIgnoreCase(subject)) {
                    double subScore1 = getSubjectScore(s1, sub);
                    double subScore2 = getSubjectScore(s2, sub);
                    if (subScore1 > subScore2) p1++;
                    else if (subScore2 > subScore1) p2++;
                }
            }
            if (p1 != p2) {
                return Integer.compare(p2, p1);
            }

            // 3. Total Aggregate Tie-Breaker
            int totalCompare = Double.compare(s2.getTotalScore(), s1.getTotalScore());
            if (totalCompare != 0) {
                return totalCompare;
            }

            // 4. Alphabetical Fallback
            return s1.getName().compareToIgnoreCase(s2.getName());
        };

        return allStudents.stream()
                .sorted(gauntletComparator)
                .limit(5)
                .collect(Collectors.toList());
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
