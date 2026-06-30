package com.hackathon.model;

/**
 * Domain model mapping a specific SIP topic to an Evaluator acting as Mentor.
 */
public class SipMapping {
    private String topic;
    private String sipMentorId;

    public SipMapping() {
    }

    public SipMapping(String topic, String sipMentorId) {
        this.topic = topic;
        this.sipMentorId = sipMentorId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSipMentorId() {
        return sipMentorId;
    }

    public void setSipMentorId(String sipMentorId) {
        this.sipMentorId = sipMentorId;
    }

    @Override
    public String toString() {
        return "SipMapping{" +
                "topic='" + topic + '\'' +
                ", sipMentorId='" + sipMentorId + '\'' +
                '}';
    }
}
