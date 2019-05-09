package com.elector.Objects.General;

import com.elector.Objects.Entities.VoterCallObject;

import java.util.Date;

public class CallDetailsObject {
    private int callOid;
    private Date date;
    private boolean answered;

    public CallDetailsObject(boolean answered) {
        this.answered = answered;
        this.date = new Date();
    }

    public CallDetailsObject(VoterCallObject callObject) {
        this.callOid = callObject.getOid();
        this.date = callObject.getTime();
        this.answered = callObject.isAnswered();
    }

    public int getCallOid() {
        return callOid;
    }

    public void setCallOid(int callOid) {
        this.callOid = callOid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
