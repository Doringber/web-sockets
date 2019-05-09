package com.elector.Objects.General;

import com.elector.Enums.ConfigEnum;
import com.elector.Utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sigal on 1/17/2017.
 */
public class EmailObject {
    private String replyTo;
    private String from;
    private List<String> to = new ArrayList<>();
    private String subject;
    private String text;
    private List<String> filesPaths = new ArrayList<>();
    private String recipient;

    public EmailObject(String replyTo, String from, String to, String subject, String text, String recipient) {
        this.replyTo = replyTo;
        this.from = from;
        this.subject = subject;
        this.text = text;
        this.to = new ArrayList<>();
        this.to.add(to);
        this.recipient = recipient;
    }

    public EmailObject(String replyTo, String from, List<String> to, String subject, String text) {
        this.replyTo = replyTo;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public EmailObject(String subject, String text, String recipient) {
        String sender = ConfigUtils.getConfig(ConfigEnum.gmail_user_name, "elector.inner@gmail.com");
        this.replyTo = sender;
        this.from = sender;
        this.subject = subject;
        this.text = text;
        this.recipient = recipient;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getFilesPaths() {
        return filesPaths;
    }

    public void setFilesPaths(List<String> filesPaths) {
        this.filesPaths = filesPaths;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
