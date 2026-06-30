package com.hackathon.model;

/**
 * Concrete class representing a Student's result details.
 * Extends the abstract Person class and implements the Gradable interface.
 */
public class Student extends Person implements Gradable {
    private String usn;
    private int batch;
    private int teamNo;
    private String topic;
    
    private double dsa;
    private double ada;
    private double dbms;
    private double math;
    private double python;
    private double javaMarks;
    private double sip;

    // Derived fields
    private double totalScore;

    public Student() {
        super();
    }

    public Student(String usn, String name, int batch, int teamNo, String topic,
                   double dsa, double ada, double dbms, double math, 
                   double python, double javaMarks, double sip) {
        super(name, usn.toLowerCase() + "@student.edu");
        this.usn = usn;
        this.batch = batch;
        this.teamNo = teamNo;
        this.topic = topic;
        this.dsa = dsa;
        this.ada = ada;
        this.dbms = dbms;
        this.math = math;
        this.python = python;
        this.javaMarks = javaMarks;
        this.sip = sip;
        updateDerivedFields();
    }

    /**
     * Helper to recalculate total whenever marks change.
     */
    public void updateDerivedFields() {
        this.totalScore = calculateTotal();
    }

    @Override
    public double calculateTotal() {
        return this.dsa + this.ada + this.dbms + this.math + this.python + this.javaMarks + this.sip;
    }

    @Override
    public double calculateAverage() {
        return calculateTotal() / 7.0;
    }

    @Override
    public String calculateGrade() {
        double avg = calculateAverage();
        if (avg >= 9.0) return "S";
        if (avg >= 8.0) return "A";
        if (avg >= 7.0) return "B";
        if (avg >= 6.0) return "C";
        if (avg >= 5.0) return "D";
        if (avg >= 4.0) return "E";
        return "F";
    }

    public String getUsn() { return usn; }
    public void setUsn(String usn) { this.usn = usn; }

    public int getBatch() { return batch; }
    public void setBatch(int batch) { this.batch = batch; }

    public int getTeamNo() { return teamNo; }
    public void setTeamNo(int teamNo) { this.teamNo = teamNo; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public double getDsa() { return dsa; }
    public void setDsa(double dsa) { this.dsa = dsa; updateDerivedFields(); }

    public double getAda() { return ada; }
    public void setAda(double ada) { this.ada = ada; updateDerivedFields(); }

    public double getDbms() { return dbms; }
    public void setDbms(double dbms) { this.dbms = dbms; updateDerivedFields(); }

    public double getMath() { return math; }
    public void setMath(double math) { this.math = math; updateDerivedFields(); }

    public double getPython() { return python; }
    public void setPython(double python) { this.python = python; updateDerivedFields(); }

    public double getJavaMarks() { return javaMarks; }
    public void setJavaMarks(double javaMarks) { this.javaMarks = javaMarks; updateDerivedFields(); }

    public double getSip() { return sip; }
    public void setSip(double sip) { this.sip = sip; updateDerivedFields(); }

    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }

    @Override
    public String toString() {
        return "Student{" +
                "usn='" + usn + '\'' +
                ", name='" + getName() + '\'' +
                ", total=" + totalScore +
                ", grade='" + calculateGrade() + '\'' +
                '}';
    }
}
