package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;
import java.util.List;

import static com.elector.Utils.Definitions.*;
import static org.apache.commons.lang.StringUtils.EMPTY;

/**
 * Created by Sigal on 7/7/2017.
 */
public class MessageObject extends BaseEntity{
    private int type;
    private VoterObject recipient;
    private String title;
    private String body;
    private Date date;
    private boolean closed;

    public MessageObject() {

    }

    public MessageObject(int type, VoterObject recipient, String title, String body, Date date) {
        this.type = type;
        this.recipient = recipient;
        this.title = title;
        this.body = body;
        this.date = date;
        this.closed = false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public VoterObject getRecipient() {
        return recipient;
    }

    public void setRecipient(VoterObject recipient) {
        this.recipient = recipient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getTemplateType() {
        String type = EMPTY;
        switch (this.type) {
            case MESSAGE_TYPE_DANGER: {
                type = "alert-danger";
                break;
            }
            case MESSAGE_TYPE_INFO: {
                type = "alert-info";
                break;
            }
            case MESSAGE_TYPE_SUCCESS: {
                type = "alert-success";
                break;
            }
            case MESSAGE_TYPE_WARNING: {
                type = "alert-warning";
                break;
            }
        }
        return type;
    }
}
