package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseUser;

import java.util.Set;

public class ObserverObject extends BaseUser {
    private Set<BallotBoxObject> ballotBoxObjectSet;
    private boolean checkedIn;
    private boolean electionDayMorningShift;
    private boolean electionDayEveningShift;
    private boolean superObserver;

    public Set<BallotBoxObject> getBallotBoxObjectSet() {
        return ballotBoxObjectSet;
    }

    public void setBallotBoxObjectSet(Set<BallotBoxObject> ballotBoxObjectSet) {
        this.ballotBoxObjectSet = ballotBoxObjectSet;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
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

    public boolean isSuperObserver() {
        return superObserver;
    }

    public void setSuperObserver(boolean superObserver) {
        this.superObserver = superObserver;
    }
}
