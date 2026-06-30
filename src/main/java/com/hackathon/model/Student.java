package com.hackathon.model;

/**
 * Concrete class representing a Student's result details.
 * Extends the abstract Person class and implements the Gradable interface.
 */
public class Student extends Person implements Gradable {
    private String usn;
    private double sub1;
    private double sub2;
    private double sub3;

    // Derived fields for easy database mapping
    private double total;
    private double average;
    private String grade;

    public Student() {
        super();
    }

    public Student(String usn, String name, String email, double sub1, double sub2, double sub3) {
        super(name, email);
        this.usn = usn;
        this.sub1 = sub1;
        this.sub2 = sub2;
        this.sub3 = sub3;
        updateDerivedFields();
    }

    /**
     * Helper to recalculate total, average, and grade whenever marks change.
     */
    public void updateDerivedFields() {
        this.total = calculateTotal();
        this.average = calculateAverage();
        this.grade = calculateGrade();
    }

    @Override
    public double calculateTotal() {
        return this.sub1 + this.sub2 + this.sub3;
    }

    @Override
    public double calculateAverage() {
        return calculateTotal() / 3.0;
    }

    @Override
    public String calculateGrade() {
        double avg = calculateAverage();
        if (avg >= 90) return "S";
        if (avg >= 80) return "A";
        if (avg >= 70) return "B";
        if (avg >= 60) return "C";
        if (avg >= 50) return "D";
        if (avg >= 40) return "E";
        return "F";
    }

    // Getters and Setters

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public double getSub1() {
        return sub1;
    }

    public void setSub1(double sub1) {
        this.sub1 = sub1;
    }

    public double getSub2() {
        return sub2;
    }

    public void setSub2(double sub2) {
        this.sub2 = sub2;
    }

    public double getSub3() {
        return sub3;
    }

    public void setSub3(double sub3) {
        this.sub3 = sub3;
    }

    public double getTotal() {
        return total;
    }

    // Explicit setters for total/avg/grade are primarily for DB loading
    public void setTotal(double total) {
        this.total = total;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "usn='" + usn + '\'' +
                ", name='" + getName() + '\'' +
                ", total=" + total +
                ", grade='" + grade + '\'' +
                '}';
    }
}
