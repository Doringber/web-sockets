package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseUser;

public class CallerObject extends BaseUser {
    private AdminUserObject adminUserObject;
    private int votersToCall;
    private boolean accessActivists;
    private boolean accessTasks;
    private boolean electionDayMorningShift;
    private boolean electionDayEveningShift;

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public int getVotersToCall() {
        return votersToCall;
    }

    public void setVotersToCall(int votersToCall) {
        this.votersToCall = votersToCall;
    }

    public void incrementVotersCount() {
        this.votersToCall++;
    }

    public boolean isAccessActivists() {
        return accessActivists;
    }

    public void setAccessActivists(boolean accessActivists) {
        this.accessActivists = accessActivists;
    }

    public boolean isAccessTasks() {
        return accessTasks;
    }

    public void setAccessTasks(boolean accessTasks) {
        this.accessTasks = accessTasks;
    }

    public boolean isElectionDayMorningShift() {
        return electionDayMorningShift;
    }

    public void setElectionDayMorningShift(boolean electionDayMorningShift) {
        this.electionDayMorningShift = electionDayMorningShift;
    }

    public boolean isElectionDayEveningShift() {
        return electionDayEveningShift;
    }

    public void setElectionDayEveningShift(boolean electionDayEveningShift) {
        this.electionDayEveningShift = electionDayEveningShift;
    }
}
