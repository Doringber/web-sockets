package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class VoterElectionDayCallObject extends BaseEntity {
    private CallerObject callerObject;
    private Date time;
    private boolean answered;
    private String comment;
    private VoterObject voterObject;
    private int voteStatus;

    public VoterElectionDayCallObject(){
    }

    public VoterElectionDayCallObject(CallerObject callerObject, VoterObject voterObject, boolean answered, int voteStatus, String comment) {
        this.time = new Date();
        this.callerObject = callerObject;
        this.answered = answered;
        this.comment = comment;
        this.voterObject = voterObject;
        this.voteStatus = voteStatus;
    }



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

    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public int getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(int voteStatus) {
        this.voteStatus = voteStatus;
    }
}
