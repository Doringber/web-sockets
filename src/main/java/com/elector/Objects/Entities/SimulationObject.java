package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Objects.General.BaseUser;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SimulationObject extends BaseEntity {
    private Date startDate;
    private Date endDate;
    private AdminUserObject adminUserObject;
    private boolean callerParticipated;
    private boolean activistParticipated;
    private boolean observerParticipated;
    private boolean driverParticipated;
    private boolean sentElectionDayAlert;
    private int totalUsersParticipated;
    private int totalVotingStatusChanges;
    private int totalElectionDayCalls;
    private int totalElectionDayAlerts;
    private Set<String> usersParticipatedKeys = new HashSet<>();

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public boolean isCallerParticipated() {
        return callerParticipated;
    }

    public void setCallerParticipated(boolean callerParticipated) {
        this.callerParticipated = callerParticipated;
    }

    public boolean isActivistParticipated() {
        return activistParticipated;
    }

    public void setActivistParticipated(boolean activistParticipated) {
        this.activistParticipated = activistParticipated;
    }

    public boolean isObserverParticipated() {
        return observerParticipated;
    }

    public void setObserverParticipated(boolean observerParticipated) {
        this.observerParticipated = observerParticipated;
    }

    public boolean isDriverParticipated() {
        return driverParticipated;
    }

    public void setDriverParticipated(boolean driverParticipated) {
        this.driverParticipated = driverParticipated;
    }

    public int getTotalUsersParticipated() {
        return totalUsersParticipated;
    }

    public void setTotalUsersParticipated(int totalUsersParticipated) {
        this.totalUsersParticipated = totalUsersParticipated;
    }

    public int getTotalVotingStatusChanges() {
        return totalVotingStatusChanges;
    }

    public void setTotalVotingStatusChanges(int totalVotingStatusChanges) {
        this.totalVotingStatusChanges = totalVotingStatusChanges;
    }

    public int getTotalElectionDayCalls() {
        return totalElectionDayCalls;
    }

    public void setTotalElectionDayCalls(int totalElectionDayCalls) {
        this.totalElectionDayCalls = totalElectionDayCalls;
    }

    public int getTotalElectionDayAlerts() {
        return totalElectionDayAlerts;
    }

    public void setTotalElectionDayAlerts(int totalElectionDayAlerts) {
        this.totalElectionDayAlerts = totalElectionDayAlerts;
    }

    public boolean isSentElectionDayAlert() {
        return sentElectionDayAlert;
    }

    public void setSentElectionDayAlert(boolean sentElectionDayAlert) {
        this.sentElectionDayAlert = sentElectionDayAlert;
    }

    public void addParticipatedUser (BaseUser user) {
        this.usersParticipatedKeys.add(String.format("%s_%s", user.getUserTypeByClass(), user.getOid()));
    }

    public Set<String> getUsersParticipatedKeys() {
        return usersParticipatedKeys;
    }

    public void setUsersParticipatedKeys(Set<String> usersParticipatedKeys) {
        this.usersParticipatedKeys = usersParticipatedKeys;
    }
}
