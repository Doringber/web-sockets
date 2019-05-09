package com.elector.Objects.Entities;


public class InnerMessageObject extends BaseMessageObject {
    private String title;
    private boolean opened;
    private boolean closed;

    public InnerMessageObject() {
    }

    public InnerMessageObject (BaseMessageObject baseMessageObject) {
        this.recipientType = baseMessageObject.recipientType;
        this.recipientOid = baseMessageObject.recipientOid;
        this.senderType = baseMessageObject.senderType;
        this.senderOid = baseMessageObject.senderOid;
        this.body = baseMessageObject.body;
        this.date = baseMessageObject.date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
