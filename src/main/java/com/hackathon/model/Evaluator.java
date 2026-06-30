package com.hackathon.model;

/**
 * Concrete class representing an Evaluator.
 * Extends Person to fulfill OOP requirements.
 */
public class Evaluator extends Person {
    private String evaluatorId;
    private String coreSubject;

    public Evaluator() {
        super();
    }

    public Evaluator(String evaluatorId, String name, String coreSubject) {
        super(name, evaluatorId.toLowerCase() + "@evaluator.edu");
        this.evaluatorId = evaluatorId;
        this.coreSubject = coreSubject;
    }

    public String getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(String evaluatorId) {
        this.evaluatorId = evaluatorId;
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
                "id='" + evaluatorId + '\'' +
                ", name='" + getName() + '\'' +
                ", subject='" + coreSubject + '\'' +
                '}';
    }
}
