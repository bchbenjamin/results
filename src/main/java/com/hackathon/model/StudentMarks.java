package com.hackathon.model;

import java.util.Arrays;
import java.util.List;

/**
 * Domain model representing a Student's academic marks across 7 subjects.
 */
public class StudentMarks {
    private String usn;
    private double dsa;
    private double ada;
    private double dbms;
    private double math;
    private double python;
    private double javaScore;
    private double sip;
    private double totalScore;

    public StudentMarks() {
    }

    public StudentMarks(String usn, double dsa, double ada, double dbms, double math, double python, double javaScore, double sip, double totalScore) {
        this.usn = usn;
        this.dsa = dsa;
        this.ada = ada;
        this.dbms = dbms;
        this.math = math;
        this.python = python;
        this.javaScore = javaScore;
        this.sip = sip;
        this.totalScore = totalScore;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public double getDsa() {
        return dsa;
    }

    public void setDsa(double dsa) {
        this.dsa = dsa;
    }

    public double getAda() {
        return ada;
    }

    public void setAda(double ada) {
        this.ada = ada;
    }

    public double getDbms() {
        return dbms;
    }

    public void setDbms(double dbms) {
        this.dbms = dbms;
    }

    public double getMath() {
        return math;
    }

    public void setMath(double math) {
        this.math = math;
    }

    public double getPython() {
        return python;
    }

    public void setPython(double python) {
        this.python = python;
    }

    public double getJavaScore() {
        return javaScore;
    }

    public void setJavaScore(double javaScore) {
        this.javaScore = javaScore;
    }

    public double getSip() {
        return sip;
    }

    public void setSip(double sip) {
        this.sip = sip;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Retrieves the score for a specified subject by its string name.
     * @param subjectName The name of the subject.
     * @return The score for that subject.
     * @throws IllegalArgumentException if the subject is not found.
     */
    public double getScoreBySubject(String subjectName) {
        switch (subjectName.toLowerCase()) {
            case "dsa": return dsa;
            case "ada": return ada;
            case "dbms": return dbms;
            case "math": return math;
            case "python": return python;
            case "java": return javaScore;
            case "sip": return sip;
            default: throw new IllegalArgumentException("Unknown subject: " + subjectName);
        }
    }

    /**
     * Retrieves a list of all recognized subject names.
     * @return List of subject names.
     */
    public static List<String> getAllSubjects() {
        return Arrays.asList("DSA", "ADA", "DBMS", "Math", "Python", "Java", "SIP");
    }

    @Override
    public String toString() {
        return "StudentMarks{" +
                "usn='" + usn + '\'' +
                ", dsa=" + dsa +
                ", ada=" + ada +
                ", dbms=" + dbms +
                ", math=" + math +
                ", python=" + python +
                ", javaScore=" + javaScore +
                ", sip=" + sip +
                ", totalScore=" + totalScore +
                '}';
    }
}
