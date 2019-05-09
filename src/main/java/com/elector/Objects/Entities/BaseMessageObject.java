package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class BaseMessageObject extends BaseEntity {
    protected int recipientType;
    protected int recipientOid;
    protected String body;
    protected Date date;
    protected int senderType;
    protected int senderOid;
    protected String senderName;

    public int getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(int recipientType) {
        this.recipientType = recipientType;
    }

    public int getRecipientOid() {
        return recipientOid;
    }

    public void setRecipientOid(int recipientOid) {
        this.recipientOid = recipientOid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public int getSenderOid() {
        return senderOid;
    }

    public void setSenderOid(int senderOid) {
        this.senderOid = senderOid;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
