package com.hackathon.model;

/**
 * Composite DTO class to pass both Student and StudentMarks context together.
 * Designed to be read-only (immutable structure) for algorithm and leaderboard services.
 */
public class StudentWithMarks {
    private final Student student;
    private final StudentMarks marks;

    public StudentWithMarks(Student student, StudentMarks marks) {
        this.student = student;
        this.marks = marks;
    }

    public Student getStudent() {
        return student;
    }

    public StudentMarks getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return "StudentWithMarks{" +
                "usn='" + student.getUsn() + '\'' +
                ", name='" + student.getName() + '\'' +
                ", totalScore=" + marks.getTotalScore() +
                '}';
    }
}
