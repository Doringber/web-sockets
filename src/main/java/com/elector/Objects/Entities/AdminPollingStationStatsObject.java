package com.elector.Objects.Entities;

import com.elector.Objects.General.BaseEntity;
import com.elector.Utils.Utils;

import java.util.Date;

import static com.elector.Utils.Definitions.*;

public class AdminPollingStationStatsObject extends BaseEntity {
    private AdminUserObject adminUserObject;
    private BallotBoxObject ballotBoxObject;
    private Date statsTime;
    private int totalVoters;
    private int totalSupporters;
    private int totalUnverifiedSupporters;
    private int totalUnknownSupportStatus;
    private int totalNotSupporting;
    private int totalVoted;
    private int totalSupportersVoted;

    public AdminPollingStationStatsObject() {
    }

    public AdminPollingStationStatsObject(AdminUserObject adminUserObject, BallotBoxObject ballotBoxObject) {
        this.adminUserObject = adminUserObject;
        this.ballotBoxObject = ballotBoxObject;
        this.statsTime = new Date();
    }

    public void addVoterToStats(VoterObject voterObject) {
        this.totalVoters++;
        switch (voterObject.getSupportStatus()) {
            case PARAM_SUPPORT_STATUS_SUPPORTING:
                this.totalSupporters++;
                break;
            case PARAM_SUPPORT_STATUS_UNVERIFIED_SUPPORTING:
                this.totalUnverifiedSupporters++;
                break;
            case PARAM_SUPPORT_STATUS_UNKNOWN:
                this.totalUnknownSupportStatus++;
                break;
            case PARAM_SUPPORT_STATUS_NOT_SUPPORTING:
                this.totalNotSupporting++;
                break;
        }
        if (voterObject.hasVoted()) {
            this.totalVoted++;
            if (voterObject.getSupportStatus() == PARAM_SUPPORT_STATUS_SUPPORTING) {
                this.totalSupportersVoted++;
            }
        }
    }

    public AdminUserObject getAdminUserObject() {
        return adminUserObject;
    }

    public void setAdminUserObject(AdminUserObject adminUserObject) {
        this.adminUserObject = adminUserObject;
    }

    public BallotBoxObject getBallotBoxObject() {
        return ballotBoxObject;
    }

    public void setBallotBoxObject(BallotBoxObject ballotBoxObject) {
        this.ballotBoxObject = ballotBoxObject;
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

    public int getTotalVoted() {
        return totalVoted;
    }

    public void setTotalVoted(int totalVoted) {
        this.totalVoted = totalVoted;
    }

    public int getTotalSupportersVoted() {
        return totalSupportersVoted;
    }

    public void setTotalSupportersVoted(int totalSupportersVoted) {
        this.totalSupportersVoted = totalSupportersVoted;
    }

    public String getPercentVoted () {
        return String.format("%.2f%%", Utils.calculatePercent(this.totalVoted, this.totalVoters));
    }

    public String getPercentSupporters () {
        return String.format("%.2f%%", Utils.calculatePercent(this.totalSupporters, this.totalVoters));
    }

    public String getPercentVotedSupporters () {
        return String.format("%.2f%%", Utils.calculatePercent(this.totalSupportersVoted, this.totalSupporters));
    }

}
