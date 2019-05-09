package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Objects.General.BaseUser;

import java.util.Date;

public class SupportStatusChangeObject extends BaseEntity {
    private VoterObject voterObject;
    private Date date;
    private Integer previousStatus;
    private Integer newStatus;
    private int initiatorType;
    private int initiatorOid;
    private String method;
    private BaseUser initiator;

    public SupportStatusChangeObject() {
    }

    public SupportStatusChangeObject(VoterObject voterObject, BaseUser initiator, Integer previousStatus, String method) {
        this.voterObject = voterObject;
        this.date = new Date();
        this.previousStatus = previousStatus;
        this.newStatus = voterObject.getSupportStatus();
        this.initiatorType = initiator.getUserType();
        this.initiatorOid = initiator.getOid();
        this.method = method;
    }

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(Integer previousStatus) {
        this.previousStatus = previousStatus;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public int getInitiatorType() {
        return initiatorType;
    }

    public void setInitiatorType(int initiatorType) {
        this.initiatorType = initiatorType;
    }

    public int getInitiatorOid() {
        return initiatorOid;
    }

    public void setInitiatorOid(int initiatorOid) {
        this.initiatorOid = initiatorOid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isSupportStatusChanged () {
        return this.previousStatus == null || (!this.previousStatus.equals(this.newStatus));
    }

    public BaseUser getInitiator() {
        return initiator;
    }

    public void setInitiator(BaseUser initiator) {
        this.initiator = initiator;
    }

    public String getInitiatorKey () {
        return String.format("%s_%s", this.initiatorType, this.initiatorOid);
    }
}
