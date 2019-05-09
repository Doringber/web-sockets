package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Objects.General.EmailObject;

import java.util.Date;

public class SentEmailObject extends BaseEntity {
    private String from;
    private String recipient;
    private String subject;
    private String text;
    private boolean success;
    private Date date;

    public SentEmailObject() {
    }

    public SentEmailObject(EmailObject emailObject, boolean success) {
        this.from = emailObject.getFrom();
        this.recipient = emailObject.getRecipient();
        this.subject = emailObject.getSubject();
        this.text = emailObject.getText();
        this.success = success;
        this.date = new Date();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
