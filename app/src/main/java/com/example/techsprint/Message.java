package com.example.techsprint;

import java.util.Date;

public class Message {
    private String text;
    private String senderEmail;
    private Date timestamp;

    // Required empty constructor for Firestore
    public Message() {
    }

    public Message(String text, String senderEmail, Date timestamp) {
        this.text = text;
        this.senderEmail = senderEmail;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
