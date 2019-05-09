package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class JobSummaryObject extends BaseEntity {
    private String name;
    private Date startTime;
    private long secondsTook;
    private boolean success;

    public JobSummaryObject(){
    }

    public JobSummaryObject(String name, Date startTime, long secondsTook, boolean success) {
        this.name = name;
        this.startTime = startTime;
        this.secondsTook = secondsTook;
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public long getSecondsTook() {
        return secondsTook;
    }

    public void setSecondsTook(long secondsTook) {
        this.secondsTook = secondsTook;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
