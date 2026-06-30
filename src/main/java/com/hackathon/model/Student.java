package com.hackathon.model;

/**
 * Domain model representing a Student.
 * Encapsulates demographic and team assignment details.
 */
public class Student {
    private String usn;
    private String name;
    private int batch;
    private int teamNumber;
    private String topic;

    public Student() {
    }

    public Student(String usn, String name, int batch, int teamNumber, String topic) {
        this.usn = usn;
        this.name = name;
        this.batch = batch;
        this.teamNumber = teamNumber;
        this.topic = topic;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Student{" +
                "usn='" + usn + '\'' +
                ", name='" + name + '\'' +
                ", batch=" + batch +
                ", teamNumber=" + teamNumber +
                ", topic='" + topic + '\'' +
                '}';
    }
}
