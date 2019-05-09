package com.elector.Models;


public class ConversationMessageModel {
    private int oid;
    private BaseUserModel sender;
    private String date;
    private String content;

    public ConversationMessageModel () {
    }


    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public BaseUserModel getSender() {
        return sender;
    }

    public void setSender(BaseUserModel sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
