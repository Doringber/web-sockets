package com.elector.Objects.Entities;

public class VoterCallObject extends CallObject {
    private VoterObject voterObject;
    private int previousStatus;
    private int newStatus;
    private Integer referrer;

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public int getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(int previousStatus) {
        this.previousStatus = previousStatus;
    }

    public int getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getReferrer() {
        return referrer;
    }

    public void setReferrer(Integer referrer) {
        this.referrer = referrer;
    }
}
