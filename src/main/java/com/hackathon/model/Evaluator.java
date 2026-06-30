package com.hackathon.model;

/**
 * Domain model representing an Evaluator and their core subject.
 */
public class Evaluator {
    private String evaluatorId;
    private String name;
    private String coreSubject;

    public Evaluator() {
    }

    public Evaluator(String evaluatorId, String name, String coreSubject) {
        this.evaluatorId = evaluatorId;
        this.name = name;
        this.coreSubject = coreSubject;
    }

    public String getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(String evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoreSubject() {
        return coreSubject;
    }

    public void setCoreSubject(String coreSubject) {
        this.coreSubject = coreSubject;
    }

    @Override
    public String toString() {
        return "Evaluator{" +
                "evaluatorId='" + evaluatorId + '\'' +
                ", name='" + name + '\'' +
                ", coreSubject='" + coreSubject + '\'' +
                '}';
    }
}
