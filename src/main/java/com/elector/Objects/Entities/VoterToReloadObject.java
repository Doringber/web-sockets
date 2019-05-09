package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class VoterToReloadObject extends BaseEntity {
    private VoterObject voterObject;
    private Date date;
    private boolean prod1;
    private boolean prod2;

    public VoterToReloadObject() {
    }

    public VoterToReloadObject(VoterObject voterObject, String activeNode) {
        this.voterObject = voterObject;
        this.date = new Date();
        if (activeNode.equals("prod1")) {
            this.prod1 = true;
        } else {
            this.prod2 = true;
        }

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

    public boolean isProd1() {
        return prod1;
    }

    public void setProd1(boolean prod1) {
        this.prod1 = prod1;
    }

    public boolean isProd2() {
        return prod2;
    }

    public void setProd2(boolean prod2) {
        this.prod2 = prod2;
    }
}
