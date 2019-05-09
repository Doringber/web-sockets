package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class SupporterVotersListItemMapObject extends BaseEntity {
    private VoterObject supporter;
    private VoterObject supporterFromList;
    private Date date;

    public SupporterVotersListItemMapObject(){
    }

    public SupporterVotersListItemMapObject(VoterObject supporter, VoterObject supporterFromList, Date date) {
        this.supporter = supporter;
        this.supporterFromList = supporterFromList;
        this.date = date;
    }

    public VoterObject getSupporter() {
        return supporter;
    }

    public void setSupporter(VoterObject supporter) {
        this.supporter = supporter;
    }

    public VoterObject getSupporterFromList() {
        return supporterFromList;
    }

    public void setSupporterFromList(VoterObject supporterFromList) {
        this.supporterFromList = supporterFromList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
