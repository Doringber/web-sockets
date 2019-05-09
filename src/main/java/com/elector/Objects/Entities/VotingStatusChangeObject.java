package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Objects.General.BaseUser;

import java.util.Date;

public class VotingStatusChangeObject extends BaseEntity {
    private VoterObject voterObject;
    private Date date;
    private boolean previousStatus;
    private boolean newStatus;
    private int initiatorType;
    private int initiatorOid;
    private int round;
    private boolean simulation;
    private int adminOid;
    private BaseUser initiator;


    public VotingStatusChangeObject(){
    }

    public VotingStatusChangeObject(VoterObject voterObject, BaseUser initiator, boolean newStatus, int round, boolean simulation, int adminOid) {
        this.voterObject = voterObject;
        this.date = new Date();
        this.previousStatus = !newStatus;
        this.newStatus = newStatus;
        this.initiatorType = initiator.getUserType();
        this.initiatorOid = initiator.getOid();
        this.round = round;
        this.simulation = simulation;
        this.adminOid = adminOid;
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

    public boolean isPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(boolean previousStatus) {
        this.previousStatus = previousStatus;
    }

    public boolean isNewStatus() {
        return newStatus;
    }

    public void setNewStatus(boolean newStatus) {
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

    public BaseUser getInitiator() {
        return initiator;
    }

    public void setInitiator(BaseUser initiator) {
        this.initiator = initiator;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public int getAdminOid() {
        return adminOid;
    }

    public void setAdminOid(int adminOid) {
        this.adminOid = adminOid;
    }
}
