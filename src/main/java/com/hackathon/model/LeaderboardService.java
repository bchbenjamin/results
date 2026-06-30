package com.hackathon.model;

import com.hackathon.dao.StudentResultDAO;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling the Leaderboard logic and the 1v1 Gauntlet tie-breaker.
 */
public class LeaderboardService {

    private final StudentResultDAO studentDao;

    public LeaderboardService() {
        this.studentDao = new StudentResultDAO();
    }

    public List<Student> getTop5Overall() {
        return studentDao.findAll().stream()
                .sorted(getOverallComparator())
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Student> getTop5ForSubject(String subject) {
        return studentDao.findAll().stream()
                .sorted(getSubjectComparator(subject.toLowerCase()))
                .limit(5)
                .collect(Collectors.toList());
    }

    private Comparator<Student> getOverallComparator() {
        return (s1, s2) -> {
            // Total Aggregate Score (descending)
            int cmp = Double.compare(s2.getTotalScore(), s1.getTotalScore());
            if (cmp != 0) return cmp;
            
            // Alphabetical sort by name
            return s1.getName().compareToIgnoreCase(s2.getName());
        };
    }

    private Comparator<Student> getSubjectComparator(String subject) {
        return (s1, s2) -> {
            // 1. Primary Sort: Compare specific target subject (descending)
            double score1 = getSubjectScore(s1, subject);
            double score2 = getSubjectScore(s2, subject);
            
            int primaryCmp = Double.compare(score2, score1);
            if (primaryCmp != 0) return primaryCmp;
            
            // 2. The 1v1 Gauntlet: Compare remaining 6 subjects
            int gauntletPoints1 = 0;
            int gauntletPoints2 = 0;
            
            String[] allSubjects = {"dsa", "ada", "dbms", "math", "python", "java", "sip"};
            for (String sub : allSubjects) {
                if (!sub.equals(subject)) {
                    double p1 = getSubjectScore(s1, sub);
                    double p2 = getSubjectScore(s2, sub);
                    if (p1 > p2) gauntletPoints1++;
                    else if (p2 > p1) gauntletPoints2++;
                }
            }
            
            if (gauntletPoints1 != gauntletPoints2) {
                // Descending (more points wins)
                return Integer.compare(gauntletPoints2, gauntletPoints1);
            }
            
            // 3. Total Aggregate
            int totalCmp = Double.compare(s2.getTotalScore(), s1.getTotalScore());
            if (totalCmp != 0) return totalCmp;
            
            // 4. Alphabetical Fallback
            return s1.getName().compareToIgnoreCase(s2.getName());
        };
    }

    public double getSubjectScore(Student s, String subject) {
        switch (subject) {
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
