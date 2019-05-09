package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;

import java.util.Date;

public class DriveObject extends BaseEntity {
    private VoterObject voterObject;
    private DriverObject driverObject;
    private Date createdTime;
    private Date requestedTime;
    private Date driveTime;
    private boolean done;
    private String startAddress;
    private boolean gotDetails;

    public DriveObject(){
    }

    public DriveObject(VoterObject voterObject){
        this.voterObject = voterObject;
    }


    public VoterObject getVoterObject() {
        return voterObject;
    }

    public void setVoterObject(VoterObject voterObject) {
        this.voterObject = voterObject;
    }

    public DriverObject getDriverObject() {
        return driverObject;
    }

    public void setDriverObject(DriverObject driverObject) {
        this.driverObject = driverObject;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(Date requestedTime) {
        this.requestedTime = requestedTime;
    }

    public Date getDriveTime() {
        return driveTime;
    }

    public void setDriveTime(Date driveTime) {
        this.driveTime = driveTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public boolean isGotDetails() {
        return gotDetails;
    }

    public void setGotDetails(boolean gotDetails) {
        this.gotDetails = gotDetails;
    }
}
