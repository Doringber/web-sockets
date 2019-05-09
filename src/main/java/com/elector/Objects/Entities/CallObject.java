package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class CallObject extends BaseEntity {
    protected CallerObject callerObject;
    protected Date time;
    protected boolean answered;
    protected String comment;
    private Integer unansweredReason;

    public CallerObject getCallerObject() {
        return callerObject;
    }

    public void setCallerObject(CallerObject callerObject) {
        this.callerObject = callerObject;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getUnansweredReason() {
        return unansweredReason;
    }

    public void setUnansweredReason(Integer unansweredReason) {
        this.unansweredReason = unansweredReason;
    }
}
