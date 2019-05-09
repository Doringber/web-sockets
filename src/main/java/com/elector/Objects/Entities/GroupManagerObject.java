package com.elector.Objects.Entities;

import com.elector.Objects.General.GeneralManagerUserObject;

import java.util.Date;

public class GroupManagerObject extends GeneralManagerUserObject {
    private DynamicGroupObject dynamicGroupObject;
    private Date creationDate;
    private Date statsTime;
    private int totalVoters;
    private int totalSupporters;
    private int totalUnverifiedSupporters;
    private int totalUnknownSupportStatus;
    private int totalNotSupporting;
    private int supportersDestination;

    public DynamicGroupObject getDynamicGroupObject() {
        return dynamicGroupObject;
    }

    public void setDynamicGroupObject(DynamicGroupObject dynamicGroupObject) {
        this.dynamicGroupObject = dynamicGroupObject;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStatsTime() {
        return statsTime;
    }

    public void setStatsTime(Date statsTime) {
        this.statsTime = statsTime;
    }

    public int getTotalVoters() {
        return totalVoters;
    }

    public void setTotalVoters(int totalVoters) {
        this.totalVoters = totalVoters;
    }

    public int getTotalSupporters() {
        return totalSupporters;
    }

    public void setTotalSupporters(int totalSupporters) {
        this.totalSupporters = totalSupporters;
    }

    public int getTotalUnverifiedSupporters() {
        return totalUnverifiedSupporters;
    }

    public void setTotalUnverifiedSupporters(int totalUnverifiedSupporters) {
        this.totalUnverifiedSupporters = totalUnverifiedSupporters;
    }

    public int getTotalUnknownSupportStatus() {
        return totalUnknownSupportStatus;
    }

    public void setTotalUnknownSupportStatus(int totalUnknownSupportStatus) {
        this.totalUnknownSupportStatus = totalUnknownSupportStatus;
    }

    public int getTotalNotSupporting() {
        return totalNotSupporting;
    }

    public void setTotalNotSupporting(int totalNotSupporting) {
        this.totalNotSupporting = totalNotSupporting;
    }

    public int getSupportersDestination() {
        return supportersDestination;
    }

    public void setSupportersDestination(int supportersDestination) {
        this.supportersDestination = supportersDestination;
    }
}
