package com.hackathon.service;

import com.hackathon.model.StudentMarks;
import com.hackathon.model.StudentWithMarks;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for leaderboard sorting and tie-breaking algorithms.
 * Encapsulates the core business logic of the 1v1 Gauntlet tie-breaker.
 */
public class LeaderboardService {

    /**
     * Retrieves the top 5 students for a specific subject, filtering out scores of 0,
     * and strictly adhering to the 4-tier tie-breaker rule.
     *
     * @param targetSubject The specific subject to sort by (e.g., "DSA", "Java").
     * @param allStudents   The complete list of all enrolled students with marks.
     * @return A sorted list of the top 5 students for the given subject.
     */
    public List<StudentWithMarks> getTopFiveBySubject(String targetSubject, List<StudentWithMarks> allStudents) {
        return allStudents.stream()
                // Filter out students with a score of 0 in the target subject
                .filter(student -> student.getMarks().getScoreBySubject(targetSubject) > 0)
                // Apply the custom 4-tier tie-breaking comparator
                .sorted(createSubjectComparator(targetSubject))
                // Take the Top 5
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the overall top 5 students based on their Total Score.
     * Tie-breaker is alphabetical by name.
     *
     * @param allStudents The complete list of all enrolled students with marks.
     * @return A sorted list of the overall top 5 students.
     */
    public List<StudentWithMarks> getOverallTopFive(List<StudentWithMarks> allStudents) {
        return allStudents.stream()
                .sorted(Comparator
                        .comparingDouble((StudentWithMarks s) -> s.getMarks().getTotalScore()).reversed()
                        .thenComparing(s -> s.getStudent().getName(), String.CASE_INSENSITIVE_ORDER))
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Creates the custom comparator implementing the 4-tier tie-breaker logic.
     */
    private Comparator<StudentWithMarks> createSubjectComparator(String targetSubject) {
        return (studentA, studentB) -> {
            StudentMarks marksA = studentA.getMarks();
            StudentMarks marksB = studentB.getMarks();

            // Tier 1: Primary Sort (Target Subject Score, Descending)
            double scoreA = marksA.getScoreBySubject(targetSubject);
            double scoreB = marksB.getScoreBySubject(targetSubject);
            int primaryComparison = Double.compare(scoreB, scoreA); // Reverse for descending
            if (primaryComparison != 0) {
                return primaryComparison;
            }

            // Tier 2: The 1v1 Gauntlet
            int gauntletPointsA = 0;
            int gauntletPointsB = 0;

            for (String subject : StudentMarks.getAllSubjects()) {
                if (subject.equalsIgnoreCase(targetSubject)) {
                    continue; // Do not compare the target subject in the gauntlet
                }
                
                double sA = marksA.getScoreBySubject(subject);
                double sB = marksB.getScoreBySubject(subject);
                
                if (sA > sB) {
                    gauntletPointsA++;
                } else if (sB > sA) {
                    gauntletPointsB++;
                }
            }

            int gauntletComparison = Integer.compare(gauntletPointsB, gauntletPointsA); // Descending
            if (gauntletComparison != 0) {
                return gauntletComparison;
            }

            // Tier 3: Total Aggregate Score (Descending)
            int totalComparison = Double.compare(marksB.getTotalScore(), marksA.getTotalScore());
            if (totalComparison != 0) {
                return totalComparison;
            }

            // Tier 4: Alphabetical Fallback (Ascending, A-Z)
            return String.CASE_INSENSITIVE_ORDER.compare(studentA.getStudent().getName(), studentB.getStudent().getName());
        };
    }
}
